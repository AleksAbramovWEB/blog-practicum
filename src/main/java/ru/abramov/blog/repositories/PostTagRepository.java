package ru.abramov.blog.repositories;


import ru.abramov.blog.models.Post;
import ru.abramov.blog.models.Tag;

import java.util.List;

public interface PostTagRepository {
    void deleteAllLinksByPost(Post post);
    void addLink(Post post, Tag tag);
    List<Tag> getTagsByPost(Post post);
}
