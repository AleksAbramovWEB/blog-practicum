package ru.abramov.blog.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("tags")
@Getter
@Setter
public class Tag {
    @Id
    private Long id;
    private String name;
}
