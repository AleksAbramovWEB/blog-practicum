package ru.abramov.blog.repositories.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.abramov.blog.models.Tag;
import ru.abramov.blog.repositories.TagRepository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TagRepositoryImpl implements TagRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Tag createByName(String name) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO tags (name) VALUES (?) returning id",
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, name);
            return ps;
        }, keyHolder);

        Tag tag = new Tag();

        tag.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        tag.setName(name);

        return tag;
    }

    @Override
    public Optional<Tag> findByName(String name) {
        String sql = "SELECT * FROM tags WHERE name = ?";

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Tag.class), name));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
