package ru.abramov.blog.services.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.abramov.blog.models.Comment;
import ru.abramov.blog.models.Post;
import ru.abramov.blog.repositories.CommentRepository;
import ru.abramov.blog.services.CommentPostService;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class CommentPostServiceImpl implements CommentPostService {

    private final CommentRepository commentRepository;

    @Override
    public void save(Comment comment) {
        commentRepository.save(comment);
    }

    @Override
    public void delete(Long commentId) {
        commentRepository.delete(commentId);
    }

    @Override
    public void mapPostComments(Post post) {
        mapPostComments(List.of(post));
    }

    @Override
    public void mapPostComments(List<Post> posts) {
        List<Long> postIds = posts.stream()
                .map(Post::getId)
                .toList();

        Map<Long, List<Comment>> mapComments = commentRepository.getCommentsByPostIds(postIds);

        posts.forEach(post -> {
            if (mapComments.containsKey(post.getId())) {
                post.setComments(mapComments.get(post.getId()));
            }
        });
    }
}
