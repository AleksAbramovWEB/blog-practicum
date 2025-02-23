package ru.abramov.blog.test.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.abramov.blog.models.Post;
import ru.abramov.blog.models.Tag;
import ru.abramov.blog.repositories.PostTagRepository;
import ru.abramov.blog.repositories.TagRepository;
import ru.abramov.blog.services.PostTagService;
import ru.abramov.blog.services.impl.PostTagServiceImpl;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringJUnitConfig(classes = PostTagServiceUnitTest.Config.class)
class PostTagServiceUnitTest {

    @Autowired
    private PostTagRepository postTagRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private PostTagService postTagService;

    private Post post;
    private Set<String> tagNames;

    @BeforeEach
    void setUp() {
        post = new Post();
        post.setId(1L);
        post.setTags(new HashSet<>(Set.of("Spring", "Java")));

        tagNames = new HashSet<>(Set.of("Spring", "Java"));
    }

    @Test
    void getPostIdsByTagName_ShouldReturnPostIds() {
        List<Long> mockPostIds = List.of(1L, 2L, 3L);
        when(postTagRepository.getPostIdsByTagName("Spring")).thenReturn(mockPostIds);

        List<Long> result = postTagService.getPostIdsByTagName("Spring");

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(mockPostIds, result);
    }

    @Test
    void mapPostTads_ShouldMapTagsToPosts() {
        List<Post> posts = List.of(post);
        Map<Long, Set<String>> mockTags = Map.of(1L, tagNames);

        when(postTagRepository.getTagsByPostIds(List.of(1L))).thenReturn(mockTags);

        postTagService.mapPostTads(posts);

        assertEquals(tagNames, post.getTags());
    }

    @Test
    void saveByPost_ShouldSaveTagsAndCreateLinks() {
        when(tagRepository.findByName("Spring")).thenReturn(Optional.empty());
        when(tagRepository.findByName("Java")).thenReturn(Optional.empty());

        Tag tagSpring = new Tag();
        tagSpring.setId(1L);
        tagSpring.setName("Spring");

        Tag tagJava = new Tag();
        tagJava.setId(2L);
        tagJava.setName("Java");

        when(tagRepository.createByName("Spring")).thenReturn(tagSpring);
        when(tagRepository.createByName("Java")).thenReturn(tagJava);

        postTagService.saveByPost(post);

        verify(postTagRepository, times(1)).deleteAllLinksByPost(post);
        verify(tagRepository, times(2)).findByName(anyString());
        verify(tagRepository, times(2)).createByName(anyString());
        verify(postTagRepository, times(2)).addLink(any(Post.class), any(Tag.class));
    }

    @Configuration
    static class Config {

        @Bean
        public PostTagRepository postTagRepository() {
            return Mockito.mock(PostTagRepository.class);
        }

        @Bean
        public TagRepository tagRepository() {
            return Mockito.mock(TagRepository.class);
        }

        @Bean
        public PostTagService postTagService(PostTagRepository postTagRepository, TagRepository tagRepository) {
            return new PostTagServiceImpl(postTagRepository, tagRepository);
        }
    }
}
