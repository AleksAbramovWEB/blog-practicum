package ru.abramov.blog.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;

@Table("post_tags")
@Getter
@Setter
public class PostTag {
    private Long postId;
    private Long tagId;
}
