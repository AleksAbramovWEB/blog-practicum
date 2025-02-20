package ru.abramov.blog.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Table("posts")
@Getter
@Setter
public class Post {
    @Id
    private Long id;
    private String imageUrl;
    private String title;
    private String content;
    private Integer countLikes;
    private LocalDateTime createdAt;

    @MappedCollection(idColumn = "post_id")
    private Set<PostTag> tags;

    @MappedCollection(idColumn = "post_id")
    private List<Comment> comments;
}