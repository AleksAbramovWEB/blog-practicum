package ru.abramov.blog.controllers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.abramov.blog.models.Post;
import ru.abramov.blog.services.PostService;
import org.springframework.ui.Model;

@Controller
@AllArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/")
    public String getPosts(Model model) {
        model.addAttribute("posts", postService.getAllPosts());
        return "posts";
    }

    @GetMapping("/post/new")
    public String createPostForm(Model model) {
        model.addAttribute("post", new Post());
        return "post";
    }

    @GetMapping("/post/{id}")
    public String getPost(@PathVariable Long id, Model model) {
        model.addAttribute("posts", postService.getPostById(id));
        return "post";
    }

    @PostMapping("/post")
    public String savePostForm(@ModelAttribute Post post, @RequestParam("image") MultipartFile imageFile, BindingResult result) {

        if (result.hasErrors()) {
            return "post";
        }

        postService.savePost(post, imageFile);

        return "redirect:/";
    }

    @PostMapping("/post/{id}/delete")
    public String deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return "redirect:/";
    }
}
