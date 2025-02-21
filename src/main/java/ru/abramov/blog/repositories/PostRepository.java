package ru.abramov.blog.repositories;

import ru.abramov.blog.models.Post;

import java.util.Optional;

public interface PostRepository {

    Post save(Post post);
    Optional<Post> findById(Long id);
}
