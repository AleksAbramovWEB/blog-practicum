package ru.abramov.blog.services;

import ru.abramov.blog.models.Post;

import java.util.List;

public interface PostService {
    List<Post> getAllPosts();
    Post getPostById(Long id);
    Post savePost(Post post);
    void addLike(Long id);
    void deletePost(Long id);
}
