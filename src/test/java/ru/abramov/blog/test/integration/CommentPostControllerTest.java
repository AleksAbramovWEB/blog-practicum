package ru.abramov.blog.test.integration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CommentPostControllerTest extends AbstractIntegrationTest {

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("INSERT INTO posts (id, title, content, image_url, count_likes, created_at) VALUES (1,'Заголовок1', 'Контент1', 'content1.jpg', 0, NOW())");
        jdbcTemplate.execute("INSERT INTO comments (id, post_id, text, created_at) VALUES (1, 1, 'New comment',NOW())");
    }

    @AfterEach
    void setDown() {
        jdbcTemplate.execute("DELETE FROM posts");
    }

    @Test
    void saveComment_ShouldCreateComment() throws Exception {
        mockMvc.perform(post("/post/1/comment")
                        .param("text", "New comment")
                        .param("post_id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/post/1"));
    }

    @Test
    void updateComment_ShouldUpdateComment() throws Exception {
        mockMvc.perform(post("/post/1/comment")
                        .param("1", "1")
                        .param("text", "New comment ddd")
                        .param("post_id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/post/1"));
    }

    @Test
    void deleteComment_ShouldDeleteComment() throws Exception {
        mockMvc.perform(post("/post/1/comment/1")
                        .param("_method", "delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/post/1"));
    }
}
