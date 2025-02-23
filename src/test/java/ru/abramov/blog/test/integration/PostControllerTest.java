package ru.abramov.blog.test.integration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import ru.abramov.blog.test.configs.TestConfig;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringJUnitConfig(classes = {TestConfig.class})
@WebAppConfiguration
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("INSERT INTO posts (id, title, content, image_url, count_likes, created_at) VALUES (1,'Заголовок1', 'Контент1', 'content1.jpg', 0, NOW())");
        jdbcTemplate.execute("INSERT INTO posts (id, title, content, image_url, count_likes, created_at) VALUES (2,'Заголовок2', 'Контент2', 'content2.jpg', 0, NOW())");
    }

    @AfterEach
    void setDown() {
        jdbcTemplate.execute("DELETE FROM posts");
    }

    @Test
    void getPosts_ShouldReturnPostsPage() throws Exception {

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("posts"))
                .andExpect(model().attributeExists("postPage"));
    }

    @Test
    void createPostForm_ShouldReturnPostForm() throws Exception {
        mockMvc.perform(get("/post/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("post"))
                .andExpect(model().attributeExists("post"));
    }

    @Test
    void getPost_ShouldReturnPostPage() throws Exception {
        mockMvc.perform(get("/post/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("post"))
                .andExpect(model().attributeExists("post"));
    }

    @Test
    void savePost_ShouldCreateNewPost() throws Exception {

        MockMultipartFile image = new MockMultipartFile(
                "image",
                "test-image.jpg",
                "image/jpeg",
                "Fake Image Content".getBytes()
        );

        mockMvc.perform(multipart("/post")
                        .file(image)
                        .param("title", "New Post")
                        .param("content", "This is a new post")
                        .param("tags", "spring", "java", "mockmvc"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void likePost_ShouldIncreaseLikeCount() throws Exception {

        mockMvc.perform(post("/post/1/like"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void deletePost_ShouldRemovePost() throws Exception {

        mockMvc.perform(post("/post/1")
                        .param("_method", "delete"))
                .andExpect(status().is3xxRedirection());
    }
}
