package ru.abramov.blog.repositories.impl;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.abramov.blog.models.Post;
import ru.abramov.blog.repositories.PostRepository;

import java.sql.PreparedStatement;
import java.sql.Statement;

@Repository
@AllArgsConstructor
public class PostRepositoryImpl implements PostRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Post save(Post post) {
        if (post.getId() == null) {

            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(
                        "INSERT INTO posts (title, content, image_url, count_likes, created_at) VALUES (?, ?, ?, 0, NOW())",
                        Statement.RETURN_GENERATED_KEYS
                );
                ps.setString(1, post.getTitle());
                ps.setString(2, post.getContent());
                ps.setString(3, post.getImageUrl());
                return ps;
            }, keyHolder);

            if (keyHolder.getKey() != null) {
                post.setId(keyHolder.getKey().longValue());
            }
        } else {
            jdbcTemplate.update(
                    "UPDATE posts SET title = ?, content = ?, image_url = ? WHERE id = ?",
                    post.getTitle(), post.getContent(), post.getImageUrl(), post.getId()
            );
        }

        return post;
    }
}
