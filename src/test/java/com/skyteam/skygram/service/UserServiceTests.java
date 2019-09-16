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
import java.time.LocalDateTime;
import java.time.LocalTime;
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
    private static final String USER_ID = "user1";

    @Before
    public void setUp() throws Exception {
        userPrincipal = mock(UserPrincipal.class);
        when(userPrincipal.getId()).thenReturn(USER_ID);
        user = new User(USER_ID);
        user.setUsername("skyteam");
        user.setFirstName("Sky");
        user.setLastName("Team");
        user.setPassword(passwordEncoder.encode("123456"));

        pageRequest = PageRequest.of(PAGE, SIZE);
    }

    @Test
    public void testAddUser_success() throws Exception {
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
        assertEquals(USER_ID, actual.getId());
        assertEquals("skyteam", actual.getUsername());
        assertEquals("Sky", actual.getFirstName());
        assertEquals("Team", actual.getLastName());
    }

    @Test
    public void testGetListUsers() throws Exception {
        when(userPrincipal.getId()).thenReturn("user2");
        User user2 = new User("user2");
        user2.setUsername("skyteam2");
        user.follow(user2);
        user2.follow(user);
        Page<User> page = new PageImpl<>(Collections.singletonList(user), pageRequest, 1);
        when(userRepository.findAll(eq(pageRequest))).thenReturn(page);
        Page<UserDTO> actual = userService.getListUsers(userPrincipal, pageRequest);
        assertEquals(0, actual.getNumber());
        assertEquals(1, actual.getTotalElements());
        assertEquals(1, actual.getContent().size());
        assertEquals(USER_ID, actual.getContent().get(0).getId());
        assertEquals("skyteam", actual.getContent().get(0).getUsername());
        assertEquals("Sky", actual.getContent().get(0).getFirstName());
        assertEquals("Team", actual.getContent().get(0).getLastName());
    }

    @Test
    public void testSearch() throws Exception {

    }

    @Test
    public void testUpdateUser() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("skyteam");
        userDTO.setFirstName("Sky");
        userDTO.setLastName("Team");
        userDTO.setBirthday(LocalDate.of(1999,1,1));
        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));
        assertTrue(userService.updateUser(userPrincipal, userDTO));
    }

    @Test
    public void testFollow() throws Exception {
        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));
        User user2 = new User("user2");
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user2));
        assertTrue(userService.follow(userPrincipal, "user2"));
        assertThat(user.getFollowings(), IsCollectionContaining.hasItem("user2"));
        assertThat(user2.getFollowers(), IsCollectionContaining.hasItem(USER_ID));
    }

    @Test
    public void testUnfollow() throws Exception {
        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));
        User user2 = new User("user2");
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user2));
        assertTrue(userService.follow(userPrincipal, "user2"));
        assertThat(user.getFollowings(), IsCollectionContaining.hasItem("user2"));
        assertThat(user2.getFollowers(), IsCollectionContaining.hasItem(USER_ID));
        assertTrue(userService.unfollow(userPrincipal, "user2"));
        assertThat(user.getFollowings(), not(IsCollectionContaining.hasItem("user2")));
        assertThat(user2.getFollowers(), not(IsCollectionContaining.hasItem(USER_ID)));
    }

    @Test
    public void testGetUser() throws Exception {
        User user2 = new User("user2");
        user2.setUsername("skyteam2");
        user.follow(user2);
        user2.follow(user);
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user2));
        UserDTO userDTO = userService.getUser(userPrincipal, "user2");
        assertEquals("user2", userDTO.getId());
        assertEquals("skyteam2", userDTO.getUsername());
    }

    @Test
    public void testGetNumOfRegistrations() throws Exception {
        LocalDate date = LocalDate.of(2019, 9, 16);
        LocalDateTime start = LocalDateTime.of(date, LocalTime.of(0, 0, 0));
        LocalDateTime end = LocalDateTime.of(date, LocalTime.of(23, 59, 59));
        when(userRepository.countBySignupDateBetween(start, end)).thenReturn(10L);
        assertEquals(10, userService.getNumOfRegistrations(date));
    }
}
