package ru.practicum.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.comment.dto.CreateCommentRequest;
import ru.practicum.comment.dto.UpdateCommentRequest;
import ru.practicum.event.EventRepository;
import ru.practicum.event.model.Event;
import ru.practicum.exception.IncorrectInputDataException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.User;
import ru.practicum.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import static ru.practicum.event.model.EventState.PUBLISHED;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {
    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Transactional
    public Comment addComment(Long userId, Long eventId, String commentText) {
        log.info("adding comment for user with id = {}, eventId = {}, commentText = {}", userId, eventId, commentText);

        User author = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("User with id =" + userId + " not found"));

        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Event with id =" + eventId + " not found"));

        if (event.getState() != PUBLISHED) {
            throw new IncorrectInputDataException("Comments are available only for published events.");
        }

        Comment comment = Comment.builder()
                        .text(commentText)
                        .author(author)
                        .event(event)
                        .created(LocalDateTime.now())
                        .build();

        log.info("saving comment {} to DB", comment);
        return commentRepository.save(comment);
    }

    @Transactional
    public Comment updateComment(Long userId, Long eventId, Long commentId, String newCommentText) {
        log.info("updating comment(userId = {}, eventId = {}, commentId = {}, new comment text = {}",
                userId, eventId, commentId, newCommentText);

        User author = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("User with id =" + userId + " not found"));

        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Event with id =" + eventId + " not found"));

        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new NotFoundException("Comment with id = " + commentId + "  not found"));
        if (!Objects.equals(comment.getAuthor().getId(), userId)) {
            throw new IncorrectInputDataException("Only comment id can edit it");
        }
        if (!Objects.equals(comment.getEvent().getId(), event.getId())) {
            throw new IncorrectInputDataException("Current comment is for the other event");
        }
        comment.setText(newCommentText);

        log.info("saving comment {} to DB", comment);
        return commentRepository.save(comment);
    }

    public List<Comment> getComments(Long eventId, Integer from, Integer size) {
        log.info("getting comments for eventId = {}, from {} of size = {}", eventId, from, size);
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Event with id =" + eventId + " not found"));
        return commentRepository.findAllByEventId(eventId, PageRequest.of(from / size, size));
    }

    public Comment getCommentById(Long commentId) {
        log.info("getting comment with id = {}", commentId);
        return commentRepository.findById(commentId).orElseThrow(() ->
                new NotFoundException("Comment with id = " + commentId + "  not found"));
    }

    @Transactional
    public void deleteComment(Long userId, Long commentId) {
        log.info("deleting comment with id = {} by author with id = {}", commentId, userId);

        User author = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("User with id =" + userId + " not found"));
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new NotFoundException("Comment with id = " + commentId + "  not found"));
        if (!Objects.equals(comment.getAuthor().getId(), author.getId())) {
            throw new IncorrectInputDataException("Only author can delete his/her comment");
        }

        commentRepository.deleteById(commentId);
    }

    @Transactional
    public void deleteCommentByAdmin(Long commentId) {
        log.info("deleting comment with id = {}", commentId);
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new NotFoundException("Comment with id = " + commentId + "  not found"));
        commentRepository.deleteById(commentId);
    }
}