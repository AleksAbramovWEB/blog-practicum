package ru.abramov.blog.test.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.abramov.blog.models.Post;
import ru.abramov.blog.models.PostPage;
import ru.abramov.blog.repositories.PostRepository;
import ru.abramov.blog.services.CommentPostService;
import ru.abramov.blog.services.ImageService;
import ru.abramov.blog.services.PostService;
import ru.abramov.blog.services.PostTagService;
import ru.abramov.blog.services.impl.PostServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringJUnitConfig(classes = PostServiceUnitTest.Config.class)
class PostServiceUnitTest {

    @Autowired
    private ImageService imageService;

    @Autowired
    private PostTagService postTagService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentPostService commentPostService;

    @Autowired
    private PostService postService;

    private Post post;

    @BeforeEach
    void setUp() {

        MockMultipartFile image = new MockMultipartFile(
                "image",
                "test-image.jpg",
                "image/jpeg",
                "Fake Image Content".getBytes()
        );

        post = new Post();
        post.setId(1L);
        post.setTitle("Test Post");
        post.setImageUrl("image.jpg");
        post.setCountLikes(0);
        post.setImage(image);

        reset(postTagService, postRepository, commentPostService);
    }

    @Test
    void getPostsWithPaginate_ShouldReturnPostPage() {
        PostPage mockPage = new PostPage(List.of(post), 0, 10, 1);

        when(postRepository.getPosts(0, 10, "test")).thenReturn(mockPage);

        PostPage result = postService.getPostsWithPaginate(0, 10, "test");

        assertNotNull(result);
        assertEquals(1, result.getContent().size());

        verify(postRepository, times(1)).getPosts(0, 10, "test");
        verify(postTagService, times(1)).mapPostTads(anyList());
        verify(commentPostService, times(1)).mapPostComments(anyList());
    }

    @Test
    void getPostById_ShouldReturnPost() {
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        Post result = postService.getPostById(1L);

        assertNotNull(result);
        assertEquals("Test Post", result.getTitle());

        verify(postRepository, times(1)).findById(1L);
        verify(postTagService, times(1)).mapPostTads(post);
        verify(commentPostService, times(1)).mapPostComments(post);
    }

    @Test
    void getPostById_ShouldThrowException_WhenPostNotFound() {
        when(postRepository.findById(2L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            postService.getPostById(2L);
        });

        assertEquals("Post not found", exception.getMessage());
    }

    @Test
    void savePost_ShouldSavePost() {
        when(imageService.save(post.getImage())).thenReturn(Optional.of("image_url"));

        when(postRepository.save(post)).thenReturn(post);

        Post savedPost = postService.savePost(post);

        assertNotNull(savedPost);
        assertEquals("image_url", savedPost.getImageUrl());

        verify(imageService, times(1)).save(post.getImage());
        verify(postRepository, times(1)).save(post);
        verify(postTagService, times(1)).saveByPost(post);
    }

    @Test
    void savePost_ShouldThrowException_WhenNoImageAndNewPost() {
        post.setImage(null);
        post.setId(null);

        when(imageService.save(null)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            postService.savePost(post);
        });

        assertEquals("Image file is required", exception.getMessage());

        verify(postRepository, never()).save(any());
    }

    @Test
    void addLike_ShouldIncrementLikes() {
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        postService.addLike(1L);

        assertEquals(1, post.getCountLikes());

        verify(postRepository, times(1)).save(post);
    }

    @Test
    void deletePost_ShouldCallRepositoryDelete() {
        postService.deletePost(1L);

        verify(postRepository, times(1)).delete(1L);
    }

    @Configuration
    static class Config {

        @Bean
        public PostRepository postRepository() {
            return Mockito.mock(PostRepository.class);
        }

        @Bean
        public PostTagService postTagService() {
            return Mockito.mock(PostTagService.class);
        }

        @Bean
        public ImageService imageService() {
            return Mockito.mock(ImageService.class);
        }

        @Bean
        public CommentPostService commentPostService() {
            return Mockito.mock(CommentPostService.class);
        }

        @Bean
        public PostService postService(ImageService imageService,  PostTagService postTagService, PostRepository postRepository, CommentPostService commentPostService) {
            return new PostServiceImpl(imageService, postTagService, postRepository, commentPostService);
        }
    }
}

