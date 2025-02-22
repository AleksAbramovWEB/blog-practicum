package ru.abramov.blog.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PostPage {

    private List<Post> content;
    private int currentPage;
    private int pageSize;
    private long totalItems;
    private int totalPages;

    public PostPage(List<Post> content, int currentPage, int pageSize, long totalItems) {
        this.content = content;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalItems = totalItems;
        this.totalPages = (int) Math.ceil((double) totalItems / pageSize);
    }
}
