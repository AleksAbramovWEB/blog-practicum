package ru.abramov.blog.services;

import ru.abramov.blog.models.Comment;
import ru.abramov.blog.models.Post;

import java.util.List;

public interface CommentPostService {
    void save(Comment comment);
    void delete(Long commentId);
    void mapPostComments(Post post);
    void mapPostComments(List<Post> post);
}
