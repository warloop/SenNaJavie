package org.example.forum.controllers.Comment;

import org.example.forum.dto.Opinions.CommentAddDto;
import org.example.forum.dto.Opinions.CommentEditDto;
import org.example.forum.entities.Comments;
import org.example.forum.repositories.Interfaces.ICommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/comments")
public class CommentController {

    private final ICommentRepository commentRepository;

    @Autowired
    public CommentController(ICommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @GetMapping("/article/{articleId}")
    public String getCommentsByArticleId(@PathVariable long articleId, Model model) {
        List<Comments> comments = commentRepository.getCommentsByArticleId(articleId);
        model.addAttribute("comments", comments);
        model.addAttribute("articleId", articleId);
        return "article-details";
    }

    @PostMapping("/add")
    public String addComment(@ModelAttribute CommentAddDto commentAddDto) {
        commentRepository.addComment(commentAddDto);
        return "redirect:/protected/articles";
    }

    @PostMapping("/edit")
    public String editComment(@ModelAttribute CommentEditDto commentEditDto) {
        commentRepository.editComment(commentEditDto);
        return "redirect:/articles/article-details/" + commentEditDto.getComment_id();
    }

    @PostMapping("/delete/{commentId}")
    public String deleteComment(@PathVariable long commentId, @RequestParam long articleId) {
        commentRepository.deleteComment(commentId);
        return "redirect:/articles/article-details/" + articleId;
    }
}
