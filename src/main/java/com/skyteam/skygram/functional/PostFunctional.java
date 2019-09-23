package com.skyteam.skygram.functional;

import com.skyteam.skygram.model.Post;
import com.skyteam.skygram.model.User;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PostFunctional {

    public static final BiFunction<List<Post>, User, List<Post>> TIMELINE_POSTS = (posts, user) -> posts.stream()
            .filter(post -> post.getAuthor().equals(user.getId()) || user.getFollowings().contains(post.getAuthor()))
            .sorted(Comparator.comparing(Post::getPostedDate, Comparator.reverseOrder()))
            .collect(Collectors.toList());

    public static final BiFunction<List<Post>, LocalDate, Long> NUM_OF_POSTS = (posts, date) -> posts.stream()
            .map(Post::getPostedDate)
            .filter(postedDate -> postedDate.toLocalDate().equals(date))
            .count();

    public static final TriFunction<List<Post>, String, Pageable, List<Post>> POST_BY_AUTHOR = (posts, userId, page) -> posts.stream()
            .filter(post -> post.getAuthor().getId().equals(userId))
            .sorted(Comparator.comparing(Post::getPostedDate, Comparator.reverseOrder()))
            .skip(page.getPageNumber() * page.getPageSize())
            .limit(page.getPageSize())
            .collect(Collectors.toList());

    public static final BiFunction<List<Post>, String, List<Post>> GET_POST_BY_HASHTAG = (posts, s) -> posts.stream()
            .filter(post -> post.getHashtags().contains(s))
            .collect(Collectors.toList());
}
