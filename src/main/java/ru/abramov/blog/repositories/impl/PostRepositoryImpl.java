package ru.abramov.blog.repositories.impl;

import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.abramov.blog.models.Post;
import ru.abramov.blog.models.PostPage;
import ru.abramov.blog.repositories.PostRepository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class PostRepositoryImpl implements PostRepository {

    private final JdbcTemplate jdbcTemplate;

    public PostPage getPosts(int page, int pageSize, List<Long> filterIds) {
        int offset = (page - 1) * pageSize;

        String sql = "SELECT * FROM posts";
        String countSql = "SELECT COUNT(*) FROM posts";
        Object[] params;

        if (filterIds != null && !filterIds.isEmpty()) {
            String inSql = String.join(",", filterIds.stream().map(id -> "?").toArray(String[]::new));
            sql += " WHERE id IN (" + inSql + ")";
            countSql += " WHERE id IN (" + inSql + ")";

            params = filterIds.toArray();
        } else {
            filterIds = new ArrayList<>();
            params = new Object[]{};
        }

        sql += " ORDER BY created_at DESC LIMIT ? OFFSET ?";
        params = appendToArray(params, pageSize, offset);

        List<Post> posts = jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Post.class), params);

        Long totalItems = jdbcTemplate.queryForObject(countSql, Long.class, filterIds.toArray());
        totalItems = (totalItems != null) ? totalItems : 0;

        return new PostPage(posts, page, pageSize, totalItems);
    }

    @Override
    public Post save(Post post) {
        if (post.getId() == null) {

            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(
                        "INSERT INTO posts (title, content, image_url, count_likes, created_at) VALUES (?, ?, ?, 0, NOW()) returning id",
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
                    "UPDATE posts SET title = ?, content = ?, image_url = ?, count_likes = ? WHERE id = ?",
                    post.getTitle(), post.getContent(), post.getImageUrl(), post.getCountLikes(), post.getId()
            );
        }

        return post;
    }

    @Override
    public Optional<Post> findById(Long id) {
        String sql = "SELECT * FROM posts WHERE id = ?";

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Post.class), id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM posts WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    private Object[] appendToArray(Object[] array, Object... elements) {
        Object[] newArray = new Object[array.length + elements.length];
        System.arraycopy(array, 0, newArray, 0, array.length);
        System.arraycopy(elements, 0, newArray, array.length, elements.length);
        return newArray;
    }
}
