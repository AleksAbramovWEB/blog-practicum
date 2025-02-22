package ru.abramov.blog.services.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.abramov.blog.models.Post;
import ru.abramov.blog.models.Tag;
import ru.abramov.blog.repositories.PostTagRepository;
import ru.abramov.blog.repositories.TagRepository;
import ru.abramov.blog.services.PostTagService;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PostTagServiceImpl implements PostTagService {

    private final PostTagRepository postTagRepository;
    private final TagRepository tagRepository;


    public Set<String> getTagNamesByPost(Post post) {
        return postTagRepository.getTagsByPost(post)
                .stream()
                .map(Tag::getName)
                .collect(Collectors.toSet());
    }


    @Transactional
    @Override
    public void saveByPost(Post post) {

        postTagRepository.deleteAllLinksByPost(post);

        Set<String> tags = post.getTags();

        for (String tagName : tags) {
            Tag tag = tagRepository.findByName(tagName)
                    .orElseGet(() -> tagRepository.createByName(tagName));

            postTagRepository.addLink(post, tag);
        }
    }
}
