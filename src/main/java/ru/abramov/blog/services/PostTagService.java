package ru.abramov.blog.services;

import ru.abramov.blog.models.Post;

import java.util.Set;

public interface PostTagService {
    void saveByPost(Post post);
    Set<String> getTagNamesByPost(Post post);
}
