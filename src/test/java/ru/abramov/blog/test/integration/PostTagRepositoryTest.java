package ru.abramov.blog.test.integration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.abramov.blog.models.Post;
import ru.abramov.blog.models.Tag;
import ru.abramov.blog.repositories.PostTagRepository;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class PostTagRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private PostTagRepository postTagRepository;

    private Post post;
    private Tag tag1;
    private Tag tag2;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DELETE FROM post_tags");
        jdbcTemplate.execute("DELETE FROM tags");
        jdbcTemplate.execute("DELETE FROM posts");


        jdbcTemplate.execute("ALTER SEQUENCE posts_id_seq RESTART WITH 1000");
        jdbcTemplate.execute("ALTER SEQUENCE tags_id_seq RESTART WITH 1000");

        jdbcTemplate.execute("INSERT INTO posts (id, title, content, image_url, count_likes, created_at) " +
                "VALUES (1, 'Test Post', 'Some content', 'test.jpg', 0, NOW())");

        jdbcTemplate.execute("INSERT INTO tags (id, name) VALUES (10, 'Spring')");
        jdbcTemplate.execute("INSERT INTO tags (id, name) VALUES (12, 'Java')");

        jdbcTemplate.execute("INSERT INTO post_tags (post_id, tag_id) VALUES (1, 10)");
        jdbcTemplate.execute("INSERT INTO post_tags (post_id, tag_id) VALUES (1, 12)");

        post = new Post();
        post.setId(1L);

        tag1 = new Tag();
        tag1.setId(1L);
        tag1.setName("Spring");

        tag2 = new Tag();
        tag2.setId(2L);
        tag2.setName("Java");
    }

    @AfterEach
    void cleanUp() {
        jdbcTemplate.execute("DELETE FROM post_tags");
        jdbcTemplate.execute("DELETE FROM tags");
        jdbcTemplate.execute("DELETE FROM posts");
    }

    @Test
    void addLink_ShouldAddTagToPost() {
        Tag newTag = new Tag();
        newTag.setId(3L);
        newTag.setName("Hibernate");

        jdbcTemplate.execute("INSERT INTO tags (id, name) VALUES (3, 'Hibernate')");

        postTagRepository.addLink(post, newTag);

        Long count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM post_tags WHERE post_id = 1 AND tag_id = 3", Long.class);
        assertEquals(1, count);
    }

    @Test
    void deleteAllLinksByPost_ShouldRemoveTagsFromPost() {
        postTagRepository.deleteAllLinksByPost(post);

        Long count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM post_tags WHERE post_id = 1", Long.class);
        assertEquals(0, count);
    }

    @Test
    void getTagsByPost_ShouldReturnTagsForPost() {
        List<Tag> tags = postTagRepository.getTagsByPost(post);

        assertNotNull(tags);
        assertEquals(2, tags.size());
        assertTrue(tags.stream().anyMatch(tag -> tag.getName().equals("Spring")));
        assertTrue(tags.stream().anyMatch(tag -> tag.getName().equals("Java")));
    }

    @Test
    void getPostIdsByTagName_ShouldReturnPostIds() {
        List<Long> postIds = postTagRepository.getPostIdsByTagName("Spring");

        assertNotNull(postIds);
        assertEquals(1, postIds.size());
        assertEquals(1L, postIds.getFirst());
    }

    @Test
    void getTagsByPostIds_ShouldReturnTagsGroupedByPost() {
        Map<Long, Set<String>> tagsByPost = postTagRepository.getTagsByPostIds(List.of(1L));

        assertNotNull(tagsByPost);
        assertTrue(tagsByPost.containsKey(1L));
        assertEquals(2, tagsByPost.get(1L).size());
        assertTrue(tagsByPost.get(1L).contains("Spring"));
        assertTrue(tagsByPost.get(1L).contains("Java"));
    }
}
