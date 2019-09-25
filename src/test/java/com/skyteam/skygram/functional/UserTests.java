package com.skyteam.skygram.functional;

import com.skyteam.skygram.common.Constants;
import com.skyteam.skygram.model.*;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.Assert.*;

@RunWith(BlockJUnit4ClassRunner.class)
public class UserTests {
    private static List<User> users = new ArrayList<User>();

    private static List<Post> posts = new ArrayList<Post>();
    @BeforeClass
    public static void beforeClass() throws Exception {
        Set<String> followers = new HashSet<String>();
        followers.add("follower1");
        User user1 = new User("1");
        user1.setUsername("user1");
        user1.setFirstName("Sky");
        user1.setLastName("Team");
        user1.setEmail("skyteam1@gmail.com");
        user1.setPhone("123456789");
        user1.setPassword(new BCryptPasswordEncoder().encode("123456"));
        user1.setBirthday(LocalDate.of(2019, 1, 1));
        user1.setSignupDate(LocalDateTime.of(2019, 1, 1, 0, 0, 0));
        user1.setRoles(Collections.singletonList(Constants.ROLE_USER));
        user1.setProfilePicture("profile_url");
        user1.setFollowers(followers);
        User user2 = new User("2");
        user2.setUsername("user1");
        user2.setFirstName("Sky");
        user2.setLastName("Team");
        user2.setEmail("skyteam1@gmail.com");
        user2.setPhone("123456789");
        user2.setPassword(new BCryptPasswordEncoder().encode("123456"));
        user2.setBirthday(LocalDate.of(2019, 1, 1));
        user2.setSignupDate(LocalDateTime.of(2019, 1, 1, 0, 0, 0));
        user2.setRoles(Collections.singletonList(Constants.ROLE_USER));
        user2.setProfilePicture("profile_url");
        users.add(user1);
        users.add(user2);

        Post post1 = new Post();
        post1.setId("5d7d6de937eae66e4c033283");
        post1.setTitle("title");
        post1.setPostedDate(LocalDateTime.of(2019, 1, 1, 0, 0, 0));
        post1.setLastModifiedDate(LocalDateTime.of(2019, 1, 1, 0, 0, 0));
        post1.setHashtags(new HashSet<>(Collections.singleton("#skyteam")));
        post1.setLocation(new Location());
        post1.setAuthor(user1);

        Post post2 = new Post();
        post2.setId("5d7d6de937eae66e4c033284");
        post2.setTitle("title");
        post2.setPostedDate(LocalDateTime.of(2019, 1, 1, 0, 0, 0));
        post2.setLastModifiedDate(LocalDateTime.of(2019, 1, 1, 0, 0, 0));
        post2.setHashtags(new HashSet<>(Collections.singleton("#skyteam")));
        post2.setLocation(new Location());
        post2.setAuthor(user1);

        Post post3 = new Post();
        post3.setId("5d7d6de937eae66e4c033285");
        post3.setTitle("title");
        post3.setPostedDate(LocalDateTime.of(2019, 1, 1, 0, 0, 0));
        post3.setLastModifiedDate(LocalDateTime.of(2019, 1, 1, 0, 0, 0));
        post3.setHashtags(new HashSet<>(Collections.singleton("#skyteam")));
        post3.setLocation(new Location());
        post3.setAuthor(user1);

        Post post4 = new Post();
        post4.setId("5d7d6de937eae66e4c033286");
        post4.setTitle("title");
        post4.setPostedDate(LocalDateTime.of(2019, 1, 1, 0, 0, 0));
        post4.setLastModifiedDate(LocalDateTime.of(2019, 1, 1, 0, 0, 0));
        post4.setHashtags(new HashSet<>(Collections.singleton("#skyteam")));
        post4.setLocation(new Location());
        post4.setAuthor(user2);

        posts.add(post1);
        posts.add(post2);
        posts.add(post3);
        posts.add(post4);
    }

    @Test
    public void testCountUsers() throws Exception {
        long a = UserFunctional.NUM_OF_USERS_HAVE_NUM_OF_POSTS.apply(users, posts, 2L);
        System.out.println(a);
        assertEquals(1L, a);
    }

    @Test
    public void testListUsersHaveMoreFollowers() throws Exception {
        List<User> a = new ArrayList<User>();
        Set<String> followers = new HashSet<String>();
        followers.add("follower1");
        User user1 = new User("1");
        user1.setUsername("user1");
        user1.setFirstName("Sky");
        user1.setLastName("Team");
        user1.setEmail("skyteam1@gmail.com");
        user1.setPhone("123456789");
        user1.setPassword(new BCryptPasswordEncoder().encode("123456"));
        user1.setBirthday(LocalDate.of(2019, 1, 1));
        user1.setSignupDate(LocalDateTime.of(2019, 1, 1, 0, 0, 0));
        user1.setRoles(Collections.singletonList(Constants.ROLE_USER));
        user1.setProfilePicture("profile_url");
        user1.setFollowers(followers);
        a.add(user1);

        List<User> b = UserFunctional.USER_HAVE_MORE_THAN_K_FOLLOWERS.apply(users, 1);
        assertEquals(a, b);
    }

    @Test
    public void testFindUser() throws Exception {
        List<User> c = UserFunctional.FIND_USER.apply(users, "ky");
        assertEquals(users, c);
    }
}
