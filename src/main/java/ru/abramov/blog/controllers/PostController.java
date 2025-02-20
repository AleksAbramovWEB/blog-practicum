package ru.abramov.blog.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PostController {

    @GetMapping("/")
    public String getPosts() {
        return "posts";
    }

    @GetMapping("/{id}")
    public String getPost(@PathVariable Long id) {
        return "post";
    }
}
