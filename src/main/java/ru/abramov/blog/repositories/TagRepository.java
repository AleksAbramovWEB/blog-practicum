package ru.abramov.blog.repositories;

import ru.abramov.blog.models.Tag;

import java.util.Optional;

public interface TagRepository {
    Optional<Tag> findByName(String name);
    Tag createByName(String name);
}
