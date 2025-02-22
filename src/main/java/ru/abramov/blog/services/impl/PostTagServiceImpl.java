package ru.abramov.blog.services.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.abramov.blog.models.Post;
import ru.abramov.blog.models.Tag;
import ru.abramov.blog.repositories.PostTagRepository;
import ru.abramov.blog.repositories.TagRepository;
import ru.abramov.blog.services.PostTagService;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@AllArgsConstructor
public class PostTagServiceImpl implements PostTagService {

    private final PostTagRepository postTagRepository;
    private final TagRepository tagRepository;

    public List<Long> getPostIdsByTagName(String tagName) {
        return postTagRepository.getPostIdsByTagName(tagName);
    }

    public void mapPostTads(Post post) {
        mapPostTads(List.of(post));
    }

    @Override
    public void mapPostTads(List<Post> posts) {
        List<Long> postIds = posts.stream()
                .map(Post::getId)
                .toList();

        Map<Long, Set<String>> tags = postTagRepository.getTagsByPostIds(postIds);

        posts.forEach(post -> {
            if (tags.containsKey(post.getId())) {
                post.setTags(tags.get(post.getId()));
            }
        });
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
