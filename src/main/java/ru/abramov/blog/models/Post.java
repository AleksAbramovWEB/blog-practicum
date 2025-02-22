package ru.abramov.blog.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Table("posts")
@Getter
@Setter
public class Post {
    @Id
    private Long id;

    private String imageUrl;

    @NotBlank(message = "Заголовок не должен быть пустым")
    @Size(max = 255, message = "Заголовок не может быть длиннее 255 символов")
    private String title;

    @NotBlank(message = "Контент не должен быть пустым")
    private String content;

    private Integer countLikes;

    private LocalDateTime createdAt;

    @NotEmpty(message = "Теги не должны быть пустыми")
    @Size(min = 1, message = "Должен быть хотя бы один тег")
    @Transient
    private Set<@NotBlank(message = "Тег не может быть пустым") String> tags = new HashSet<>();

    @MappedCollection(idColumn = "post_id")
    private List<Comment> comments;

    @Transient
    private MultipartFile image;
}