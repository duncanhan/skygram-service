package com.skyteam.skygram.service;

import com.skyteam.skygram.dto.UserDTO;
import com.skyteam.skygram.dto.UserRequestDTO;
import com.skyteam.skygram.model.User;
import com.skyteam.skygram.repository.UserRepository;
import com.skyteam.skygram.security.UserPrincipal;
import org.hamcrest.core.IsCollectionContaining;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTests {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    private User user;
    private UserPrincipal userPrincipal;
    private final int PAGE = 0;
    private final int SIZE = 15;
    private Pageable pageRequest;

    @Before
    public void setUp() throws Exception {
        userPrincipal = mock(UserPrincipal.class);
        when(userPrincipal.getId()).thenReturn("user1");
        user = new User("user1");
        user.setUsername("skyteam");
        user.setFirstName("Sky");
        user.setLastName("Team");
        user.setPassword(passwordEncoder.encode("123456"));

        pageRequest = PageRequest.of(PAGE, SIZE);
    }

    @Test
    public void testAddUser_success() {
        UserRequestDTO userRequestDTO = new UserRequestDTO(
                "skyteam",
                "Sky",
                "Team",
                "skyteam@gmail.com",
                "123456789",
                LocalDate.of(2000, 1, 1),
                "123456"
        );
        when(userRepository.save(any(User.class))).thenReturn(user);
        UserDTO actual = userService.addUser(userRequestDTO);
        assertEquals("user1", actual.getId());
        assertEquals("skyteam", actual.getUsername());
        assertEquals("Sky", actual.getFirstName());
        assertEquals("Team", actual.getLastName());
    }

    @Test
    public void testGetListUsers() {
        Page<User> emptyPage = new PageImpl<>(Collections.singletonList(user), pageRequest, 1);
        when(userRepository.findAll(eq(pageRequest))).thenReturn(emptyPage);
        Page<UserDTO> actual = userService.getListUsers(userPrincipal, pageRequest);
        assertEquals(0, actual.getNumber());
        assertEquals(1, actual.getTotalElements());
        assertEquals(1, actual.getContent().size());
        assertEquals("user1", actual.getContent().get(0).getId());
        assertEquals("skyteam", actual.getContent().get(0).getUsername());
        assertEquals("Sky", actual.getContent().get(0).getFirstName());
        assertEquals("Team", actual.getContent().get(0).getLastName());
    }

    @Test
    public void testSearch() {

    }

    @Test
    public void testUpdateUser() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("skyteam");
        userDTO.setFirstName("Sky");
        userDTO.setLastName("Team");
        userDTO.setBirthday(LocalDate.of(1999,1,1));
        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));
        assertTrue(userService.updateUser(userPrincipal, userDTO));
    }

    @Test
    public void testFollow() {
        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));
        User user2 = new User("user2");
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user2));
        assertTrue(userService.follow(userPrincipal, "user2"));
        assertThat(user.getFollowings(), IsCollectionContaining.hasItem("user2"));
        assertThat(user2.getFollowers(), IsCollectionContaining.hasItem("user1"));
    }

    @Test
    public void testUnfollow() {
        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));
        User user2 = new User("user2");
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user2));
        assertTrue(userService.follow(userPrincipal, "user2"));
        assertThat(user.getFollowings(), IsCollectionContaining.hasItem("user2"));
        assertThat(user2.getFollowers(), IsCollectionContaining.hasItem("user1"));
        assertTrue(userService.unfollow(userPrincipal, "user2"));
        assertThat(user.getFollowings(), not(IsCollectionContaining.hasItem("user2")));
        assertThat(user2.getFollowers(), not(IsCollectionContaining.hasItem("user1")));
    }

    @Test
    public void testGetUser() {
        User user2 = new User("user2");
        user2.setUsername("skyteam2");
        user.getFollowings().add("user1");
        user.getFollowers().add("user1");
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user2));
        UserDTO userDTO = userService.getUser(userPrincipal, "user2");
        assertEquals("user2", userDTO.getId());
        assertEquals("skyteam2", userDTO.getUsername());
    }
}
