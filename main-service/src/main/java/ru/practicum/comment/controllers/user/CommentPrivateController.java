package ru.practicum.comment.controllers.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.Comment;
import ru.practicum.comment.CommentMapper;
import ru.practicum.comment.CommentService;
import jakarta.validation.Valid;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.CreateCommentRequest;
import ru.practicum.comment.dto.UpdateCommentRequest;

@Validated
@RestController
@RequestMapping("/users/{userId}/comments")
@RequiredArgsConstructor
public class CommentPrivateController {
    private final CommentService commentService;
    private final CommentMapper mapper;

    @PostMapping("/{eventId}")
    @ResponseStatus(value = HttpStatus.CREATED)
    public CommentDto addComment(@PathVariable Long userId,
                                 @PathVariable Long eventId,
                                 @Valid @RequestBody CreateCommentRequest request) {
        Comment savedComment = commentService.addComment(userId, eventId, request);
        return mapper.toDto(savedComment);
    }

    @PatchMapping("/{eventId}/{commentId}")
    public CommentDto updateComment(@PathVariable Long userId,
                                    @PathVariable Long eventId,
                                    @PathVariable Long commentId,
                                    @Valid @RequestBody UpdateCommentRequest request) {
        Comment updatedComment = commentService.updateComment(userId, eventId, commentId, request);
        return mapper.toDto(updatedComment);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Long userId,
                              @PathVariable Long commentId) {
        commentService.deleteComment(userId, commentId);
    }
}