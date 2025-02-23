package ru.abramov.blog.repositories.impl;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.abramov.blog.models.Comment;
import ru.abramov.blog.repositories.CommentRepository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@AllArgsConstructor
public class CommentRepositoryImpl implements CommentRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Comment save(Comment comment) {
        if (comment.getId() == null) {

            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(
                        "INSERT INTO comments (post_id, text, created_at) VALUES (?, ?, NOW()) returning id",
                        Statement.RETURN_GENERATED_KEYS
                );
                ps.setLong(1, comment.getPostId());
                ps.setString(2, comment.getText());
                return ps;
            }, keyHolder);

            if (keyHolder.getKey() != null) {
                comment.setId(keyHolder.getKey().longValue());
            }
        } else {
            jdbcTemplate.update(
                    "UPDATE comments SET text = ? WHERE id = ?",
                    comment.getText(), comment.getId()
            );
        }

        return comment;
    }

    @Override
    public void delete(Long commentId) {
        String sql = "DELETE FROM comments WHERE id = ?";
        jdbcTemplate.update(sql, commentId);
    }

    @Override
    public Map<Long, List<Comment>> getCommentsByPostIds(List<Long> postIds) {
        if (postIds == null || postIds.isEmpty()) {
            return Collections.emptyMap();
        }

        String inSql = String.join(",", Collections.nCopies(postIds.size(), "?"));
        String sql = "SELECT * FROM comments WHERE post_id IN (" + inSql + ") ORDER BY created_at DESC";

        List<Comment> comments = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Comment.class), postIds.toArray());

        return comments.stream().collect(Collectors.groupingBy(Comment::getPostId));
    }
}
