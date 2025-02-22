package ru.abramov.blog.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
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
        model.addAttribute("post", postService.getPostById(id));
        return "post";
    }

    @PostMapping("/post/{id}/like")
    public String likePost(@PathVariable Long id) {
        postService.addLike(id);

        return "redirect:/post/" + id;
    }

    @PostMapping("/post")
    public String savePostForm(@Valid @ModelAttribute Post post, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("post", post);
            return "post";
        }

        postService.savePost(post);

        return "redirect:/";
    }

    @PostMapping("/post/{id}/delete")
    public String deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return "redirect:/";
    }
}
