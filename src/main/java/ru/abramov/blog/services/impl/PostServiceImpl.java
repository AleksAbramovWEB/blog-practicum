package ru.abramov.blog.services.impl;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.abramov.blog.models.Post;
import ru.abramov.blog.repositories.PostRepository;
import ru.abramov.blog.services.CommentPostService;
import ru.abramov.blog.services.ImageService;
import ru.abramov.blog.services.PostService;
import ru.abramov.blog.services.PostTagService;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PostServiceImpl implements PostService {

    private final ImageService imageService;
    private final PostTagService postTagService;
    private final PostRepository postRepository;
    private final CommentPostService commentPostService;

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

        Post post = postOptional.get();

        postTagService.mapPostTads(post);
        commentPostService.mapPostComments(post);

        return post;
    }

    @Override
    @Transactional
    public Post savePost(Post post) {

        Optional<String> path = imageService.save(post.getImage());

        if (path.isEmpty() && post.getId() == null) {
            throw new IllegalArgumentException("Image file is required");
        }

        path.ifPresent(post::setImageUrl);

        postTagService.saveByPost(post);

        return postRepository.save(post);
    }

    @Override
    public void addLike(Long id) {
        Post post = getPostById(id);

        post.setCountLikes(post.getCountLikes() + 1);

        postRepository.save(post);
    }

    @Override
    public void deletePost(Long id) {
        postRepository.delete(id);
    }
}
