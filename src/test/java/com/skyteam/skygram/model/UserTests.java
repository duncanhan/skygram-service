package com.skyteam.skygram.model;

import com.skyteam.skygram.common.Constants;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.Assert.*;

@RunWith(BlockJUnit4ClassRunner.class)
public class UserTests {

    private static User follower;

    private static User followee;

    @BeforeClass
    public static void beforeClass() throws Exception {
        follower = new User("follower");
        follower.setUsername("skyteam1");
        follower.setFirstName("Sky");
        follower.setLastName("Team");
        follower.setEmail("skyteam1@gmail.com");
        follower.setPhone("123456789");
        follower.setPassword(new BCryptPasswordEncoder().encode("123456"));
        follower.setBirthday(LocalDate.of(2019, 1, 1));
        follower.setSignupDate(LocalDateTime.of(2019, 1, 1, 0, 0, 0));
        follower.setRoles(Collections.singletonList(Constants.ROLE_USER));
        follower.setProfilePicture("profile_url");
        followee = new User();
        followee.setId("followee");
    }

    @Test
    public void testNewUser() {
        assertNotNull(follower);
        assertNotNull(followee);
        assertEquals("follower", follower.getId());
        assertEquals("skyteam1", follower.getUsername());
        assertEquals("Sky", follower.getFirstName());
        assertEquals("Team", follower.getLastName());
        assertEquals("skyteam1@gmail.com", follower.getEmail());
        assertEquals("123456789", follower.getPhone());
        assertTrue(new BCryptPasswordEncoder().matches("123456", follower.getPassword()));
        assertEquals(LocalDate.of(2019, 1, 1), follower.getBirthday());
        assertEquals(LocalDateTime.of(2019, 1, 1, 0, 0, 0), follower.getSignupDate());
        assertEquals(1, follower.getRoles().size());
        assertEquals("profile_url", follower.getProfilePicture());
    }

    @Test
    public void testFollow() {
        follower.follow(followee);
        assertNotNull(follower.getFollowings());
        assertNotNull(followee.getFollowers());
        assertTrue(follower.getFollowings().contains(followee.getId()));
        assertTrue(followee.getFollowers().contains(follower.getId()));
    }

    @Test
    public void testUnfollow() {
        follower.follow(followee);
        follower.unfollow(followee);
        assertNotNull(follower.getFollowings());
        assertNotNull(followee.getFollowers());
        assertFalse(follower.getFollowings().contains(followee.getId()));
        assertFalse(followee.getFollowers().contains(follower.getId()));
    }

    @Test
    public void testGetNumOfFollowers() {
        follower.follow(followee);
        assertEquals(1, follower.getNumOfFollowings());
        assertEquals(1, followee.getNumOfFollowers());
    }
}
