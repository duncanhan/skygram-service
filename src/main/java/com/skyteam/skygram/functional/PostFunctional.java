package com.skyteam.skygram.functional;

import com.skyteam.skygram.model.Comment;
import com.skyteam.skygram.model.Media;
import com.skyteam.skygram.model.Post;
import com.skyteam.skygram.model.User;
import com.skyteam.skygram.service.impl.PostServiceImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PostFunctional {

    /**
     * Paging a list of posts
     */
    public static final BiFunction<List<Post>, Pageable, List<Post>> PAGING_POSTS = (posts, page) ->
            posts.stream()
                    .skip(page.getPageNumber() * page.getPageSize())
                    .limit(page.getPageSize())
                    .collect(Collectors.toList());

    /**
     * Get posts of user and their followings
     */
    public static final BiFunction<List<Post>, User, List<Post>> TIMELINE_POSTS = (posts, user) ->
            posts.stream()
                    .filter(post -> post.getAuthor().equals(user) || user.getFollowings().contains(post.getAuthor().getId()))
                    .sorted(Comparator.comparing(Post::getPostedDate, Comparator.reverseOrder()))
                    .collect(Collectors.toList());

    /**
     * Get number of posts created in date
     */
    public static final BiFunction<List<Post>, LocalDate, Long> NUM_OF_POSTS = (posts, date) ->
            posts.stream()
                    .map(Post::getPostedDate)
                    .filter(postedDate -> date.equals(postedDate.toLocalDate()))
                    .count();

    /**
     * Get posts of author sort by creation date reverse
     */
    public static final TriFunction<List<Post>, String, Pageable, List<Post>> POSTS_BY_AUTHOR = (posts, userId, page) ->
            PAGING_POSTS.apply(
                    posts.stream()
                            .filter(post -> post.getAuthor().getId().equals(userId))
                            .sorted(Comparator.comparing(Post::getPostedDate, Comparator.reverseOrder()))
                            .collect(Collectors.toList()), page);

    /**
     * Get top most popular hashtags
     */
    public static final BiFunction<List<Post>, Integer, List<String>> TOP_K_TRENDING_HASHTAGS = (posts, k) ->
            posts.stream()
                    .flatMap(post -> post.getHashtags().stream())
                    .collect(Collectors.groupingBy(String::valueOf, Collectors.counting()))
                    .entrySet()
                    .stream()
                    .sorted(Comparator.comparing(Map.Entry::getValue, Comparator.reverseOrder()))
                    .map(Map.Entry::getKey)
                    .limit(k)
                    .collect(Collectors.toList());

    /**
     * Update post pipeline
     */
    public static final TriFunction<MultipartFile[], Post, PostServiceImpl, Post> UPDATE_POST = (files, post, serv) -> {
        String postId = post.getId();
        BiFunction<Object, Integer, Object> check = (f, c) -> {
            try {
                return serv.upload((MultipartFile) f, c, postId);
            } catch (Exception e) {
                return e.getMessage();
            }
        };
        Stream.iterate(0, x -> x + 1).limit(files.length)
                .forEach(i -> post.updateMedia((Media) check.apply(files[i], i)));
        return post;
    };

    /**
     * Get posts by hashtag
     */
    public static final BiFunction<List<Post>, String, List<Post>> POSTS_BY_HASHTAG = (posts, s) ->
            posts.stream()
                    .filter(post -> post.getHashtags().contains(s))
                    .collect(Collectors.toList());

    /**
     * Get top k most liked posts
     */
    public static final BiFunction<List<Post>, Long, List<Post>> TOP_K_MOST_LIKED_POSTS = (posts, k) ->
            posts.stream()
                    .sorted(Comparator.comparing(Post::getNumOfLikes, Comparator.reverseOrder()))
                    .limit(k)
                    .collect(Collectors.toList());

    /**
     * Get top k comments with length for user on date
     */
    public static final TetraFunction<User, List<Post>, LocalDate, Long, List<Comment>> TOP_K_COMMENTS_BY_LENGTH_FOR_USER_ON_DATE = (user, posts, date, k) ->
            posts.stream()
                    .flatMap(p -> p.getComments().stream())
                    .filter(c -> c.getAuthor().equals(user) && c.getCreatedDate() != null && c.getCreatedDate().toLocalDate().equals(date))
                    .sorted(Comparator.comparing(c -> c.getText().length(), Comparator.reverseOrder()))
                    .limit(k)
                    .collect(Collectors.toList());

    /**
     * Get top k posts with most comments
     */
    public static final BiFunction<List<Post>, Long, List<Post>> GET_MOST_COMMENTED_POSTS = (posts, k) ->
            posts.stream()
                    .sorted(Comparator.comparing(post -> post.getComments().size(), Comparator.reverseOrder()))
                    .limit(k)
                    .collect(Collectors.toList());

    /**
     * Update comment
     */
    public static final FiveFunctional<LocalDateTime, List<Comment>, String, String, String, Boolean> UPDATE_COMMENT = (new_date, comments, cid, uid, nc) -> {
        Function<Comment, Integer> commentFunc = (c) -> { // (comments, comment_id, user_id, new_comment)
            c.setText(nc);
            c.setLastModifiedDate(new_date); // LocalDateTime.now()
            return 1;
        };

        Stream<Comment> x = comments.stream();
        return x.map(c -> cid.equals(c.getId().toString()) &&
                c.getAuthor().getId().equals(uid) ?
                commentFunc.apply(c) : 0).anyMatch(any -> any == 1);
    };

    public static final TriFunction<List<Comment>, String, String, List<Comment>> DELETE_COMMENT = (comments, cid, uid) ->
            comments.stream()
                    .filter(c -> !(cid.equals(c.getId().toString()) &&
                            c.getAuthor().getId().equals(uid)))
                    .collect(Collectors.toList());

    /**
     * Get most liked post,  which have more than x comments,
     * and one of the comments is of users with email contains ‘s’
     */
    public static final TetraFunction<List<Post>, Long, Long, String, List<Post>> GET_MOST_LIKED_POSTS_HAVING_COMMENTS_FROM_EMAIL
            = (posts, noOfPosts, noOfComments, emailString) ->
            posts.stream()
                    .filter(post -> post.getComments().size() > noOfComments)
                    .filter(post -> post.getComments().stream().anyMatch(comment -> comment.getAuthor().getEmail().contains(emailString)))
                    .sorted(Comparator.comparing(Post::getNumOfLikes, Comparator.reverseOrder()))
                    .limit(noOfPosts)
                    .collect(Collectors.toList());

    /**
     * Get top k hashtags start with 's' and sort by number of posts
     */
    public static final TriFunction<List<Post>, String, Integer, List<String>> TOP_K_HASHTAGS_START_WITH = (posts, s, k) ->
            posts.stream()
                    .flatMap(post -> post.getHashtags().stream().filter(hashtag -> hashtag.startsWith(s)))
                    .collect(Collectors.groupingBy(String::valueOf, Collectors.counting()))
                    .entrySet()
                    .stream()
                    .sorted(Comparator.comparing(Map.Entry::getValue, Comparator.reverseOrder()))
                    .map(Map.Entry::getKey)
                    .limit(k)
                    .collect(Collectors.toList());
}
