package ru.abramov.blog.repositories.impl;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.abramov.blog.models.Post;
import ru.abramov.blog.models.Tag;
import ru.abramov.blog.repositories.PostTagRepository;

import java.util.List;

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
}
