package com.skyteam.skygram.functional;

import com.skyteam.skygram.model.Comment;
import com.skyteam.skygram.model.Media;
import com.skyteam.skygram.model.Comment;
import com.skyteam.skygram.model.Post;
import com.skyteam.skygram.model.User;
import com.skyteam.skygram.service.impl.PostServiceImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public static final TriFunction<MultipartFile[], Post, PostServiceImpl, Post> UPDATE_POST = (files, post, serv) -> {
        String postId = post.getId();
        BiFunction<Object, Integer, Object> check = (f, c) -> {
            try{return serv.upload((MultipartFile) f,c,postId);}
            catch(Exception e){return e.getMessage();}
        };
        Stream.iterate(0, x->x+1).limit(files.length)
                .forEach(i -> post.updateMedia((Media)check.apply(files[i], i)));
        return post;
    };

    public static final BiFunction<List<Post>, String, List<Post>> GET_POST_BY_HASHTAG = (posts, s) -> posts.stream()
            .filter(post -> post.getHashtags().contains(s))
            .collect(Collectors.toList());

    public static final BiFunction<List<Post>, Long, List<Post>> MOST_LIKED_K_POSTS = (posts, k) -> posts
            .stream()
            .sorted((p1, p2) -> p2.getLikes() == null ? 0 : p2.getLikes().size() - p1.getLikes().size())
            .limit(k)
            .collect(Collectors.toList());

    public static final TetraFunction<User, List<Post>, LocalDate, Long, List<Comment>> TOP_K_COMMENTS_BY_LENGTH_FOR_USER_ON_DATE = (user, posts, date, k) -> posts
            .stream()
            .filter(p -> p.getAuthor().equals(user) && p.getPostedDate().toLocalDate().equals(date))
            .flatMap(p -> p.getComments().stream())
            .sorted((c1, c2) -> c2.getText().length() - c1.getText().length())
            .limit(k)
            .collect(Collectors.toList());

    public static final BiFunction<List<Post>, Long, List<Post>> GET_MOST_COMMENTED_POSTS = (posts, k) -> posts.stream()
            .sorted(Comparator.comparingInt(post -> post.getComments().size()))
            .limit(k)
            .collect(Collectors.toList());

    public static final QuadriFunction<List<Comment>, String, String, String, Boolean> UPDATE_COMMENT = (cmts, cid, uid, nc) -> {

        Function<Comment, Boolean> commentFunc = (c) -> {
            c.setText(nc);
            c.setLastModifiedDate(LocalDateTime.now());
            return true;
        };
        return cmts.stream().map(c -> cid.equals(c.getId().toString()) &&
                c.getAuthor().getId().equals(uid) ?
                commentFunc.apply(c): false ).reduce(false, (r, i) -> i ? true: i||r);
    };
}
