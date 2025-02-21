package ru.abramov.blog.services.impl;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.abramov.blog.models.Post;
import ru.abramov.blog.models.PostTag;
import ru.abramov.blog.models.Tag;
import ru.abramov.blog.repositories.PostRepository;
import ru.abramov.blog.repositories.PostTagRepository;
import ru.abramov.blog.repositories.TagRepository;
import ru.abramov.blog.services.ImageService;
import ru.abramov.blog.services.PostService;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class PostServiceImpl implements PostService {

    private final ImageService imageService;
    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final PostTagRepository postTagRepository;

    @Override
    public List<Post> getAllPosts() {
        return List.of();
    }

    @Override
    public Post getPostById(Long id) {
        return null;
    }

    @Override
    public void savePost(Post post, MultipartFile imageFile) {

        Optional<String> path = imageService.save(imageFile);

        path.ifPresent(post::setImageUrl);

        post = postRepository.save(post);

        for (PostTag postTag : post.getPostTags()) {

            String tagName = postTag.getTag().getName().trim().toLowerCase();

            Tag tag = tagRepository.findByName(tagName).orElseGet(() -> {
                Tag newTag = new Tag();
                newTag.setName(tagName);
                return tagRepository.save(newTag);
            });

            PostTag newPostTag = new PostTag();
            newPostTag.setPostId(post.getId());
            newPostTag.setTagId(tag.getId());
            postTagRepository.save(newPostTag);
        }
    }

    @Override
    public void deletePost(Long id) {

    }


}
