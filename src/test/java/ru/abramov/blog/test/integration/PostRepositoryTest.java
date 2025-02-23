package ru.abramov.blog.test.integration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import ru.abramov.blog.models.Post;
import ru.abramov.blog.models.PostPage;
import ru.abramov.blog.repositories.PostRepository;
import ru.abramov.blog.test.configs.TestConfig;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig(classes = {TestConfig.class})
@WebAppConfiguration
public class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("ALTER SEQUENCE posts_id_seq RESTART WITH 1000");

        jdbcTemplate.execute("INSERT INTO posts (id, title, content, image_url, count_likes, created_at) " +
                "VALUES (1, 'First Post', 'Content 1', 'image1.jpg', 10, NOW())");
        jdbcTemplate.execute("INSERT INTO posts (id, title, content, image_url, count_likes, created_at) " +
                "VALUES (2, 'Second Post', 'Content 2', 'image2.jpg', 5, NOW())");
    }

    @AfterEach
    void cleanUp() {
        jdbcTemplate.execute("DELETE FROM posts");
    }

    @Test
    void save_ShouldInsertNewPost() {
        Post newPost = new Post();
        newPost.setTitle("New Post");
        newPost.setContent("New Content");
        newPost.setImageUrl("new.jpg");

        Post savedPost = postRepository.save(newPost);

        assertNotNull(savedPost.getId());

        Long count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM posts", Long.class);
        assertEquals(3, count);
    }

    @Test
    void save_ShouldUpdateExistingPost() {
        Post existingPost = new Post();
        existingPost.setId(1L);
        existingPost.setTitle("Updated Title");
        existingPost.setContent("Updated Content");
        existingPost.setImageUrl("updated.jpg");
        existingPost.setCountLikes(15);

        postRepository.save(existingPost);

        String updatedTitle = jdbcTemplate.queryForObject("SELECT title FROM posts WHERE id = 1", String.class);
        assertEquals("Updated Title", updatedTitle);
    }

    @Test
    void findById_ShouldReturnPost_WhenExists() {
        Optional<Post> result = postRepository.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("First Post", result.get().getTitle());
    }

    @Test
    void findById_ShouldReturnEmpty_WhenNotFound() {
        Optional<Post> result = postRepository.findById(99L);

        assertTrue(result.isEmpty());
    }

    @Test
    void delete_ShouldRemovePost() {
        postRepository.delete(1L);

        Long count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM posts WHERE id = 1", Long.class);
        assertEquals(0, count);
    }

    @Test
    void getPosts_ShouldReturnPaginatedPosts() {
        PostPage result = postRepository.getPosts(1, 1, null);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(2, result.getTotalItems());
    }

    @Test
    void getPosts_ShouldApplyFilter() {
        jdbcTemplate.execute("INSERT INTO tags (id, name) VALUES (5, 'Spring')");
        jdbcTemplate.execute("INSERT INTO post_tags (post_id, tag_id) VALUES (1, 5)");

        PostPage result = postRepository.getPosts(1, 10, "Spring");

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("First Post", result.getContent().getFirst().getTitle());
    }
}
