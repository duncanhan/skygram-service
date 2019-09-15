package com.skyteam.skygram.model;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(BlockJUnit4ClassRunner.class)
public class UserTests {

    private User follower;

    private User followee;

    @Before
    public void setUp() throws Exception {
        follower = new User("follower");
        followee = new User("followee");
    }

    @Test
    public void testNewUser() {
        assertNotNull(follower);
        assertNotNull(followee);
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
