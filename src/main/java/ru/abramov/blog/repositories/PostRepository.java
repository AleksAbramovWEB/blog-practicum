package ru.abramov.blog.repositories;

import ru.abramov.blog.models.Post;
import ru.abramov.blog.models.PostPage;

import java.util.Optional;

public interface PostRepository {
    PostPage getPosts(int page, int pageSize, String filter);
    Post save(Post post);
    Optional<Post> findById(Long id);
    void delete(Long id);
}
