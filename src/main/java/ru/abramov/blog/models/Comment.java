package ru.abramov.blog.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("comments")
@Getter
@Setter
public class Comment {
    @Id
    private Long id;
    private Long postId;
    private String text;
    private LocalDateTime createdAt;
}
