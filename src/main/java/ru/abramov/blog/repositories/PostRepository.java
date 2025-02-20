package ru.abramov.blog.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.abramov.blog.models.Post;

@Repository
public interface PostRepository extends CrudRepository<Post, Integer> {
}
