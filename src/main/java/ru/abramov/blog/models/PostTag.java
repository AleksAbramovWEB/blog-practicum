package ru.abramov.blog.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Objects;

@Table("post_tags")
@Getter
@Setter
public class PostTag {
    private Long postId;
    private Long tagId;

    @MappedCollection(idColumn = "tag_id")
    private Tag tag;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostTag postTag = (PostTag) o;
        return Objects.equals(postId, postTag.postId) &&
                Objects.equals(tagId, postTag.tagId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId, tagId);
    }
}
