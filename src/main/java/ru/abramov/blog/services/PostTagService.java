package ru.abramov.blog.services;

import ru.abramov.blog.models.Post;

import java.util.List;

public interface PostTagService {
    void saveByPost(Post post);
    void mapPostTads(Post post);
    void mapPostTads(List<Post> post);
    List<Long> getPostIdsByTagName(String tagName);
}
