package ru.practicum.comment.controllers.user;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.Comment;
import ru.practicum.comment.CommentMapper;
import ru.practicum.comment.CommentService;
import ru.practicum.comment.dto.CommentDto;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Positive;
import java.util.List;

@Validated
@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentPublicController {
    private final CommentService commentService;
    private final CommentMapper mapper;

    @GetMapping("/event/{eventId}")
    List<CommentDto> getComments(@PathVariable Long eventId,
                                 @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                 @RequestParam(value = "size", defaultValue = "10") @Positive Integer size) {
        List<Comment> comments = commentService.getComments(eventId, from, size);
        return mapper.toDto(comments);
    }

    @GetMapping("/{commentId}")
    CommentDto getCommentById(@PathVariable Long commentId) {
        Comment comment = commentService.getCommentById(commentId);
        return mapper.toDto(comment);
    }
}