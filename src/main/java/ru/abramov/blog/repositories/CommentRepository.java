package ru.abramov.blog.repositories;


import ru.abramov.blog.models.Comment;

import java.util.List;
import java.util.Map;

public interface CommentRepository{
    Comment save(Comment comment);
    void delete(Long commentId);
    Map<Long, List<Comment>> getCommentsByPostIds(List<Long> postIds);
}
