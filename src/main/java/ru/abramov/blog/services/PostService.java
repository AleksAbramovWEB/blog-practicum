package ru.abramov.blog.services;

import ru.abramov.blog.models.Post;
import ru.abramov.blog.models.PostPage;

public interface PostService {
    PostPage getPostsWithPaginate(int page, int pageSize, String filter);
    Post getPostById(Long id);
    Post savePost(Post post);
    void addLike(Long id);
    void deletePost(Long id);
}
