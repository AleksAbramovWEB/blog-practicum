package ru.abramov.blog.test.integration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.abramov.blog.models.Tag;
import ru.abramov.blog.repositories.TagRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class TagRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private TagRepository tagRepository;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("ALTER SEQUENCE tags_id_seq RESTART WITH 1000");

        jdbcTemplate.execute("INSERT INTO tags (id, name) VALUES (21, 'Spring Boot')");
        jdbcTemplate.execute("INSERT INTO tags (id, name) VALUES (22, 'Java')");
    }

    @AfterEach
    void cleanUp() {
        jdbcTemplate.execute("DELETE FROM tags");
    }

    @Test
    void createByName_ShouldInsertNewTag() {
        Tag tag = tagRepository.createByName("Hibernate");

        assertNotNull(tag);
        assertNotNull(tag.getId());
        assertEquals("Hibernate", tag.getName());

        Long count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM tags WHERE name = 'Hibernate'", Long.class);
        assertEquals(1, count);
    }

    @Test
    void findByName_ShouldReturnTag_WhenExists() {
        Optional<Tag> result = tagRepository.findByName("Spring Boot");

        assertTrue(result.isPresent());
        assertEquals("Spring Boot", result.get().getName());
    }

    @Test
    void findByName_ShouldReturnEmpty_WhenNotExists() {
        Optional<Tag> result = tagRepository.findByName("NonExistent");

        assertTrue(result.isEmpty());
    }
}
