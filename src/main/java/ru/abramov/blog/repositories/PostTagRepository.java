package ru.abramov.blog.repositories;


import ru.abramov.blog.models.Post;
import ru.abramov.blog.models.Tag;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface PostTagRepository {
    void deleteAllLinksByPost(Post post);
    void addLink(Post post, Tag tag);
    List<Tag> getTagsByPost(Post post);
    List<Long> getPostIdsByTagName(String tagName);
    Map<Long, Set<String>> getTagsByPostIds(List<Long> postIds);
}
