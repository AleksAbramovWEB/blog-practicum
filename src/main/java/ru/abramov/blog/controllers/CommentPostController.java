package ru.abramov.blog.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.abramov.blog.models.Comment;
import ru.abramov.blog.services.CommentPostService;

@Controller
@AllArgsConstructor
public class CommentPostController {

    private final CommentPostService commentPostService;

    @PostMapping("/post/{postId}/comment")
    public String savePostForm(@Valid @ModelAttribute Comment comment, @PathVariable Long postId) {

        commentPostService.save(comment);

        return "redirect:/post/" + postId;
    }

    @PostMapping(value = "/post/{postId}/comment/{commentId}", params = "_method=delete")
    public String deletePost(@PathVariable Long postId, @PathVariable Long commentId) {

        commentPostService.delete(commentId);

        return "redirect:/post/" + postId;
    }
}
