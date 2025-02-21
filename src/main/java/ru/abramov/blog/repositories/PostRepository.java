package ru.abramov.blog.repositories;

import org.springframework.stereotype.Repository;
import ru.abramov.blog.models.Post;

public interface PostRepository {

    Post save(Post post);
}
