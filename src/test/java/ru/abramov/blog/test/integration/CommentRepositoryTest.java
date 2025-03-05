package ru.abramov.blog.test.integration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.abramov.blog.models.Comment;
import ru.abramov.blog.repositories.CommentRepository;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class CommentRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private CommentRepository commentRepository;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("ALTER SEQUENCE posts_id_seq RESTART WITH 1000");
        jdbcTemplate.execute("ALTER SEQUENCE comments_id_seq RESTART WITH 1000");

        jdbcTemplate.execute("INSERT INTO posts (id, title, content, image_url, count_likes, created_at) VALUES (1,'Test Post', 'Some content', 'test.jpg', 0, NOW())");
        jdbcTemplate.execute("INSERT INTO comments (id, post_id, text, created_at) VALUES (1, 1, 'First comment', NOW())");
        jdbcTemplate.execute("INSERT INTO comments (id, post_id, text, created_at) VALUES (2, 1, 'Second comment', NOW())");
    }

    @AfterEach
    void cleanUp() {
        jdbcTemplate.execute("DELETE FROM comments");
        jdbcTemplate.execute("DELETE FROM posts");
    }

    @Test
    void save_ShouldInsertNewComment() {
        Comment newComment = new Comment();
        newComment.setPostId(1L);
        newComment.setText("New Comment");

        Comment savedComment = commentRepository.save(newComment);

        assertNotNull(savedComment.getId());

        Long count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM comments WHERE post_id = 1", Long.class);
        assertEquals(3, count);
    }

    @Test
    void save_ShouldUpdateExistingComment() {
        Comment existingComment = new Comment();
        existingComment.setId(1L);
        existingComment.setPostId(1L);
        existingComment.setText("Updated Comment");

        commentRepository.save(existingComment);

        String updatedText = jdbcTemplate.queryForObject("SELECT text FROM comments WHERE id = 1", String.class);
        assertEquals("Updated Comment", updatedText);
    }

    @Test
    void delete_ShouldRemoveComment() {
        commentRepository.delete(1L);

        Long count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM comments WHERE id = 1", Long.class);
        assertEquals(0, count);
    }

    @Test
    void getCommentsByPostIds_ShouldReturnCommentsGroupedByPost() {
        Map<Long, List<Comment>> result = commentRepository.getCommentsByPostIds(List.of(1L));

        assertNotNull(result);
        assertTrue(result.containsKey(1L));
        assertEquals(2, result.get(1L).size());
    }
}

