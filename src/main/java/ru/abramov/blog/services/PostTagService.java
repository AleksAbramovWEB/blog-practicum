package ru.abramov.blog.services;

import ru.abramov.blog.models.Post;

public interface PostTagService {
    void saveByPost(Post post);
    void mapPostTads(Post post);
}
