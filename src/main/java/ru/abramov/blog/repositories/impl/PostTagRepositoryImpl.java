package ru.abramov.blog.repositories.impl;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.abramov.blog.models.Post;
import ru.abramov.blog.models.Tag;
import ru.abramov.blog.repositories.PostTagRepository;

import java.util.*;

@Repository
@AllArgsConstructor
public class PostTagRepositoryImpl implements PostTagRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void deleteAllLinksByPost(Post post) {
        String sql = "DELETE FROM post_tags WHERE post_id = ?";
        jdbcTemplate.update(sql, post.getId());
    }

    @Override
    public void addLink(Post post, Tag tag) {
        String sql = "INSERT INTO post_tags (post_id, tag_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, post.getId(), tag.getId());
    }

    public List<Tag> getTagsByPost(Post post) {
        String sql = "SELECT t.* FROM post_tags pt INNER JOIN tags t on t.id = pt.tag_id WHERE post_id = ?";

        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Tag.class), post.getId());
    }

    public List<Long> getPostIdsByTagName(String tagName) {
        String sql = "SELECT pt.post_id FROM post_tags pt INNER JOIN tags t on t.id = pt.tag_id WHERE t.name ilike ?";

        return jdbcTemplate.queryForList(sql, Long.class, "%" + tagName + "%");
    }

    @Override
    public Map<Long, Set<String>> getTagsByPostIds(List<Long> postIds) {
        if (postIds == null || postIds.isEmpty()) {
            return Collections.emptyMap();
        }

        String inSql = String.join(",", Collections.nCopies(postIds.size(), "?"));
        String sql = "SELECT pt.post_id, t.name FROM post_tags pt INNER JOIN tags t on t.id = pt.tag_id WHERE pt.post_id in (" + inSql + ")";

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, postIds.toArray());

        Map<Long, Set<String>> tagsByPost = new HashMap<>();

        for (Map<String, Object> row : rows) {
            Long postId = ((Number) row.get("post_id")).longValue();
            String tagName = (String) row.get("name");

            tagsByPost.computeIfAbsent(postId, k -> new HashSet<>()).add(tagName);
        }

        return tagsByPost;
    }
}
