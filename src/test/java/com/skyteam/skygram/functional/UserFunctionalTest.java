package com.skyteam.skygram.functional;

import com.skyteam.skygram.model.Post;
import com.skyteam.skygram.model.User;
import org.hamcrest.collection.IsEmptyCollection;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.Assert.*;

@RunWith(BlockJUnit4ClassRunner.class)
public class UserFunctionalTest {

    private List<User> users = new ArrayList<>();

    private static List<Post> posts = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        User user1 = new User("user1");
        user1.setUsername("skygram");
        user1.setFirstName("sky");
        user1.setLastName("gram");
        user1.setSignupDate(LocalDateTime.of(2019, 9, 10, 1, 0, 0));
        User user2 = new User("user2");
        user2.setUsername("gramsky");
        user2.setFirstName("gram");
        user2.setLastName("sky");
        user2.setSignupDate(LocalDateTime.of(2019, 9, 10, 13, 0, 0));
        User user3 = new User("user3");
        user3.setUsername("mpp");
        user3.setFirstName("mp");
        user3.setLastName("p");
        user3.setSignupDate(LocalDateTime.of(2019, 10, 10, 13, 0, 0));
        User user4 = new User("user4");
        user4.setUsername("mum");
        user4.setFirstName("mu");
        user4.setLastName("m");
        user4.setSignupDate(LocalDateTime.of(2020, 10, 10, 13, 0, 0));
        users.addAll(Arrays.asList(user1, user2, user3, user4));

        Post post1 = new Post();
        post1.setAuthor(user1);
        Post post2 = new Post();
        post2.setAuthor(user1);
        Post post3 = new Post();
        post3.setAuthor(user1);
        Post post4 = new Post();
        post4.setAuthor(user2);
        posts.addAll(Arrays.asList(post1, post2, post3, post4));
    }

    @Test
    public void test_PAGING_USERS() throws Exception {
        List<User> us = UserFunctional.PAGING_USERS.apply(users, PageRequest.of(0, 10));
        assertEquals(4, us.size());
        assertEquals("user1", us.get(0).getId());
        assertEquals("user2", us.get(1).getId());
        assertEquals("user3", us.get(2).getId());
        assertEquals("user4", us.get(3).getId());
    }

    @Test
    public void test_MOST_FOLLOWED_K_USERS() throws Exception {
        List<User> mostFollowedKUsers = UserFunctional.MOST_FOLLOWED_K_USERS.apply(users, 3L);
        assertEquals(3, mostFollowedKUsers.size());

        users.get(3).follow(users.get(2));
        mostFollowedKUsers = UserFunctional.MOST_FOLLOWED_K_USERS.apply(users, 1L);
        assertTrue(mostFollowedKUsers.contains(users.get(2)));

        users.get(2).follow(users.get(0));
        users.get(1).follow(users.get(2));
        mostFollowedKUsers = UserFunctional.MOST_FOLLOWED_K_USERS.apply(users, 3L);
        assertTrue(mostFollowedKUsers.contains(users.get(0)));
        assertTrue(mostFollowedKUsers.contains(users.get(1)));
        assertEquals(users.get(2), mostFollowedKUsers.get(0));
    }

    @Test
    public void test_USERNAME_START_WITH_OK() throws Exception {
        List<User> us = UserFunctional.USERNAME_START_WITH.apply(users, "sky");
        assertEquals(1, us.size());
        assertEquals("skygram", us.get(0).getUsername());
    }

    @Test
    public void test_USERNAME_START_WITH_NOT_FOUND() throws Exception {
        List<User> us = UserFunctional.USERNAME_START_WITH.apply(users, "cdoan");
        assertThat(us, IsEmptyCollection.empty());
    }

    @Test
    public void test_NUM_OF_REGISTRATIONS() throws Exception {
        long numOfRegistrations = UserFunctional.NUM_OF_REGISTRATIONS.apply(users, LocalDate.of(2019, 9, 10));
        assertEquals(2, numOfRegistrations);
        long numOfRegistrations2 = UserFunctional.NUM_OF_REGISTRATIONS.apply(users, LocalDate.of(2019, 9, 11));
        assertEquals(0, numOfRegistrations2);
    }

    @Test
    public void test_UNION() throws Exception {
        Set<String> set1 = new HashSet<>(Arrays.asList("1", "2", "3"));
        Set<String> set2 = new HashSet<>(Arrays.asList("1", "2", "5", "6"));
        long union = UserFunctional.UNION.apply(set1, set2);
        assertEquals(2, union);
    }

    @Test
    public void test_RATING() throws Exception {
        Set<String> set1 = new HashSet<>(Arrays.asList("1", "2", "3", "4", "99", "100"));
        Set<String> set2 = new HashSet<>(Arrays.asList("1", "2", "5", "6", "66", "101"));
        double rating = UserFunctional.RATING.apply(set1, set2);
        assertEquals(0.2, rating, 0.0);
    }

    @Test
    public void test_TOP_K_SUGGESTION_USERS() throws Exception {
        User user = new User("user5");
        user.follow(users.get(0));
        user.follow(users.get(1));
        users.add(user);
        users.get(2).follow(users.get(0));
        users.get(3).follow(users.get(0));
        users.get(3).follow(users.get(1));
        List<User> topKUsers = UserFunctional.TOP_K_SUGGESTION_USERS.apply(users, user, 1);
        assertEquals(1, topKUsers.size());
        assertEquals("user4", topKUsers.get(0).getId());

        List<User> topKUsers1 = UserFunctional.TOP_K_SUGGESTION_USERS.apply(users, user, 10);
        assertEquals(2, topKUsers1.size());
        assertEquals("user4", topKUsers1.get(0).getId());
        assertEquals("user3", topKUsers1.get(1).getId());
    }

    @Test
    public void test_MUTUAL_FOLLOWERS() throws Exception {
        User user = new User("user5");
        user.follow(users.get(0));
        user.follow(users.get(1));
        user.follow(users.get(2));
        users.add(user);
        List<User> mutualFollowers = UserFunctional.MUTUAL_FOLLOWERS.apply(users, users.get(0), users.get(1));
        assertTrue(mutualFollowers.contains(user));
    }

    @Test
    public void test_NUM_OF_USERS_HAVE_NUM_OF_POSTS() throws Exception {
        long a = UserFunctional.NUM_OF_USERS_HAVE_NUM_OF_POSTS.apply(users, posts, 2L);
        assertEquals(2L, a);
    }

    @Test
    public void test_USER_HAVE_MORE_THAN_K_FOLLOWERS() throws Exception {
        users.get(1).follow(users.get(0));
        users.get(2).follow(users.get(0));
        users.get(3).follow(users.get(0));
        users.get(2).follow(users.get(1));
        users.get(3).follow(users.get(1));
        List<User> b = UserFunctional.USER_HAVE_MORE_THAN_K_FOLLOWERS.apply(users, 3);
        assertEquals(1, b.size());
        assertEquals(users.get(0), b.get(0));
    }

    @Test
    public void test_FIND_USER() throws Exception {
        List<User> c = UserFunctional.FIND_USER.apply(users, "ky");
        assertEquals(2, c.size());
        assertEquals(users.get(0), c.get(0));
    }
}
