package com.skyteam.skygram.functional;

import com.skyteam.skygram.dto.SearchResponseDTO;
import com.skyteam.skygram.model.Post;
import com.skyteam.skygram.model.User;

import java.time.LocalDate;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UserFunctional {

    public static final BiFunction<List<User>, String, List<SearchResponseDTO>> USERNAME_START_WITH = (users, s) ->
            Optional.ofNullable(users).orElseGet(Collections::emptyList).stream()
                    .filter(user -> user.getUsername().startsWith(s))
                    .map(user -> new SearchResponseDTO(user.getId(), null, user.getUsername(), user.getFirstName() + " " + user.getLastName()))
                    .collect(Collectors.toList());

    public static final BiFunction<List<User>, Long, List<User>> MOST_FOLLOWED_K_USERS = (users, k) -> users
            .stream()
            .sorted((u1, u2) -> u2.getFollowers().size() - u1.getFollowers().size())
            .limit(k)
            .collect(Collectors.toList());

    public static final BiFunction<List<User>, LocalDate, Long> NUM_OF_REGISTRATIONS = (users, date) ->
            users.stream()
                    .map(User::getSignupDate)
                    .filter(signupDate -> signupDate.toLocalDate().equals(date))
                    .count();

    public static final BiFunction<Set<String>, Set<String>, Long> UNION = (ids1, ids2) ->
            ids1.stream().filter(ids2::contains).count();

    public static final BiFunction<Set<String>, Set<String>, Double> RATING = (ids1, ids2) ->
            (double) UNION.apply(ids1, ids2) / Stream.concat(ids1.stream(), ids2.stream()).count() - UNION.apply(ids1, ids2);

    public static final TriFunction<List<User>, User, Integer, List<User>> TOP_K_SUGGESTION_USERS = (users, user, k) ->
            users.stream()
                    .filter(u -> !u.equals(user))
                    .sorted(Comparator.comparingDouble(u -> RATING.apply(u.getFollowings(), user.getFollowings())))
                    .limit(k)
                    .collect(Collectors.toList());

    public static final TriFunction<List<User>, User, User, List<User>> MUTUAL_FOLLOWERS = (users, user1, user2) ->
            users.stream()
                    .filter(u -> user1.getFollowers().contains(u.getId()) && user2.getFollowers().contains(u.getId()))
                    .collect(Collectors.toList());

    public static final TriFunction<List<User>, List<Post>, Long, Long> NUM_OF_USERS_HAVE_NUM_OF_POSTS = (users, posts, k) -> users.stream()
            .filter(user -> posts.stream().filter(post -> post.getAuthor().equals(user)).count() > k).count();

    public static final BiFunction<List<User>, Integer, List<User>> USER_HAVE_MORE_THAN_K_FOLLOWERS = (users, k) -> users.stream()
            .filter(user -> user.getFollowers().size() >= k).collect(Collectors.toList());

    public static final BiFunction<List<User>, String, List<User>> FIND_USER = (users, string) -> users.stream()
            .filter(user -> user.getUsername().toLowerCase().contains(string.toLowerCase())
                    || user.getFirstName().toLowerCase().contains(string.toLowerCase())
                    || user.getLastName().toLowerCase().contains(string.toLowerCase()))
            .collect(Collectors.toList());
}
