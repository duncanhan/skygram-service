package com.skyteam.skygram.functional;

import com.skyteam.skygram.model.Post;
import com.skyteam.skygram.model.User;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UserFunctional {

    /**
     * Paging user list
     */
    public static final BiFunction<List<User>, Pageable, List<User>> PAGING_USERS = (users, page) ->
            users.stream()
                    .skip(page.getPageNumber() * page.getPageSize())
                    .limit(page.getPageSize())
                    .collect(Collectors.toList());

    /**
     * Get users that username start with string 's'
     */
    public static final BiFunction<List<User>, String, List<User>> USERNAME_START_WITH = (users, s) ->
            users.stream()
                    .filter(user -> user.getUsername().startsWith(s))
                    .sorted(Comparator.comparing(User::getFirstName))
                    .collect(Collectors.toList());

    /**
     * Get top k users most followed
     */
    public static final BiFunction<List<User>, Long, List<User>> MOST_FOLLOWED_K_USERS = (users, k) ->
            users.stream()
                    .sorted(Comparator.comparing(u -> u.getFollowers().size(), Comparator.reverseOrder()))
                    .limit(k)
                    .collect(Collectors.toList());

    /**
     * Get number of registrations on date
     */
    public static final BiFunction<List<User>, LocalDate, Long> NUM_OF_REGISTRATIONS = (users, date) ->
            users.stream()
                    .map(User::getSignupDate)
                    .filter(signupDate -> signupDate.toLocalDate().equals(date))
                    .count();

    public static final BiFunction<Set<String>, Set<String>, Long> UNION = (ids1, ids2) ->
            ids1.stream().filter(ids2::contains).count();

    public static final BiFunction<Set<String>, Set<String>, Double> RATING = (ids1, ids2) ->
            (double) UNION.apply(ids1, ids2) / (Stream.concat(ids1.stream(), ids2.stream()).count() - UNION.apply(ids1, ids2));

    /**
     * Get top k suggested users based on number of mutual following
     */
    public static final TriFunction<List<User>, User, Integer, List<User>> TOP_K_SUGGESTION_USERS = (users, user, k) ->
            users.stream()
                    .filter(u -> !u.equals(user) && !user.getFollowings().contains(u.getId()))
                    .sorted(Comparator.comparing(u -> RATING.apply(u.getFollowings(), user.getFollowings()), Comparator.reverseOrder()))
                    .limit(k)
                    .collect(Collectors.toList());

    /**
     * Get mutual followers between users
     */
    public static final TriFunction<List<User>, User, User, List<User>> MUTUAL_FOLLOWERS = (users, user1, user2) ->
            users.stream()
                    .filter(u -> user1.getFollowers().contains(u.getId()) && user2.getFollowers().contains(u.getId()))
                    .collect(Collectors.toList());

    /**
     * Number of users that have greater than k posts
     */
    public static final TriFunction<List<User>, List<Post>, Long, Long> NUM_OF_USERS_HAVE_NUM_OF_POSTS = (users, posts, k) ->
            users.stream()
                    .filter(user -> posts.stream()
                            .filter(post -> post.getAuthor().equals(user)).count() > k)
                    .count();

    /**
     * Get users that have more than k (>=) followers
     */
    public static final BiFunction<List<User>, Integer, List<User>> USER_HAVE_MORE_THAN_K_FOLLOWERS = (users, k) ->
            users.stream()
                    .filter(user -> user.getFollowers().size() >= k)
                    .collect(Collectors.toList());

    /**
     * Find users by username/first name/last name
     */
    public static final BiFunction<List<User>, String, List<User>> FIND_USER = (users, string) ->
            users.stream()
                    .filter(user -> user.getUsername().toLowerCase().contains(string.toLowerCase())
                            || user.getFirstName().toLowerCase().contains(string.toLowerCase())
                            || user.getLastName().toLowerCase().contains(string.toLowerCase()))
                    .collect(Collectors.toList());
}
