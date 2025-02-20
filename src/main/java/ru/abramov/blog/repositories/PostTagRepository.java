package ru.abramov.blog.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.abramov.blog.models.PostTag;

@Repository
public interface PostTagRepository extends CrudRepository<PostTag, Long> {
}
