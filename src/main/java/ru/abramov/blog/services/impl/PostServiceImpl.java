package ru.abramov.blog.services.impl;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.abramov.blog.models.Post;
import ru.abramov.blog.repositories.PostRepository;
import ru.abramov.blog.services.ImageService;
import ru.abramov.blog.services.PostService;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PostServiceImpl implements PostService {

    private final ImageService imageService;
    private final PostRepository postRepository;

    @Override
    public List<Post> getAllPosts() {
        return List.of();
    }

    @Override
    public Post getPostById(Long id) {

        Optional<Post> postOptional = postRepository.findById(id);

        if (postOptional.isEmpty()) {
            throw new IllegalArgumentException("Post not found");
        }

        return postOptional.get();
    }

    @Override
    public Post savePost(Post post) {

        Optional<String> path = imageService.save(post.getImage());

        if (path.isEmpty() && post.getId() == null) {
            throw new IllegalArgumentException("Image file is required");
        }

        path.ifPresent(post::setImageUrl);

        return postRepository.save(post);
    }

    @Override
    public void deletePost(Long id) {

    }


}
