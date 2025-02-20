package ru.abramov.blog.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.abramov.blog.models.Comment;

@Repository
public interface CommentRepository extends CrudRepository<Comment, Long> {
}
