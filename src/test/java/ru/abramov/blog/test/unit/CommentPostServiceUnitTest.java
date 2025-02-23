package ru.abramov.blog.test.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.abramov.blog.models.Comment;
import ru.abramov.blog.models.Post;
import ru.abramov.blog.repositories.CommentRepository;
import ru.abramov.blog.services.CommentPostService;
import ru.abramov.blog.services.impl.CommentPostServiceImpl;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringJUnitConfig(classes = CommentPostServiceUnitTest.Config.class)
class CommentPostServiceUnitTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentPostService commentPostService;

    private Post post;

    private Comment comment;

    @BeforeEach
    void setUp() {

        reset(commentRepository);

        post = new Post();
        post.setId(1L);
        post.setTitle("Test Post");
        post.setTitle("Content Post");

        comment = new Comment();
        comment.setId(10L);
        comment.setText("Test Comment");
    }

    @Test
    void save_ShouldCallRepositorySave() {
        commentPostService.save(comment);

        verify(commentRepository, times(1)).save(comment);
    }

    @Test
    void delete_ShouldCallRepositoryDelete() {
        commentPostService.delete(10L);

        verify(commentRepository, times(1)).delete(10L);
    }

    @Test
    void mapPostComments_ShouldMapCommentsToSinglePost() {
        when(commentRepository.getCommentsByPostIds(List.of(1L)))
                .thenReturn(Map.of(1L, List.of(comment)));

        commentPostService.mapPostComments(post);

        assertNotNull(post.getComments());
        assertEquals(1, post.getComments().size());
        assertEquals("Test Comment", post.getComments().getFirst().getText());

        verify(commentRepository, times(1)).getCommentsByPostIds(List.of(1L));
    }

    @Test
    void mapPostComments_ShouldMapCommentsToMultiplePosts() {
        Post post2 = new Post();
        post2.setId(2L);
        post2.setTitle("Second Post");

        Comment comment2 = new Comment();
        comment2.setId(11L);
        comment2.setText("Another Comment");
        comment2.setPostId(post2.getId());

        List<Post> posts = List.of(post, post2);
        Map<Long, List<Comment>> mockComments = Map.of(
                1L, List.of(comment),
                2L, List.of(comment2)
        );

        when(commentRepository.getCommentsByPostIds(List.of(1L, 2L)))
                .thenReturn(mockComments);

        commentPostService.mapPostComments(posts);

        assertEquals(1, post.getComments().size());
        assertEquals("Test Comment", post.getComments().getFirst().getText());

        assertEquals(1, post2.getComments().size());
        assertEquals("Another Comment", post2.getComments().getFirst().getText());

        verify(commentRepository, times(1)).getCommentsByPostIds(List.of(1L, 2L));
    }

    @Configuration
    static class Config {
        @Bean
        public CommentRepository commentRepository() {
            return Mockito.mock(CommentRepository.class);
        }

        @Bean
        public CommentPostService commentPostService(CommentRepository commentRepository) {
            return new CommentPostServiceImpl(commentRepository);
        }
    }
}
