package com.skyteam.skygram.functional;

import com.skyteam.skygram.model.User;
import com.skyteam.skygram.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static com.skyteam.skygram.functional.UserFunctional.MOST_FOLLOWED_K_USERS;
import static org.junit.Assert.assertTrue;

public class UserFunctionalTest {

    List<User> users = new ArrayList<>();

    @MockBean
    private UserRepository userRepository;

    @Before
    public void setUp() throws Exception {
        for (int i = 1; i < 11; i++) {
            users.add(new User(String.valueOf(i)));
        }
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testMostFollowedUsers () {
        Long k = 3L;
        List<User> mostFollowedKUsers = MOST_FOLLOWED_K_USERS.apply(users, k);
        assertTrue(mostFollowedKUsers.size() == k);

        users.get(1).getFollowers().add("2");
        mostFollowedKUsers = MOST_FOLLOWED_K_USERS.apply(users, k);

        assertTrue(mostFollowedKUsers.contains(users.get(1)));

        users.get(2).getFollowers().add("1");
        users.get(2).getFollowers().add("3");
        mostFollowedKUsers = MOST_FOLLOWED_K_USERS.apply(users, k);

        assertTrue(mostFollowedKUsers.contains(users.get(1)));
        assertTrue(mostFollowedKUsers.contains(users.get(2)));
        assertTrue(mostFollowedKUsers.get(0).equals(users.get(2)));
    }

}
