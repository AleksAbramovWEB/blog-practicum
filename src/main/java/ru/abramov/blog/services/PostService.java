package ru.abramov.blog.services;

import org.springframework.web.multipart.MultipartFile;
import ru.abramov.blog.models.Post;

import java.util.List;

public interface PostService {
    List<Post> getAllPosts();
    Post getPostById(Long id);
    void savePost(Post post, MultipartFile imageFile);
    void deletePost(Long id);
}
