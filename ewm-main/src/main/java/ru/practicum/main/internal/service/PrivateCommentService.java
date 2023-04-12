package ru.practicum.main.internal.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.common.dto.CommentDTO;
import ru.practicum.main.common.dto.CommentShortDTO;
import ru.practicum.main.common.dto.NewCommentDTO;
import ru.practicum.main.common.exception.NotFoundException;
import ru.practicum.main.common.mapper.CommentMapper;
import ru.practicum.main.common.model.Comment;
import ru.practicum.main.common.model.Event;
import ru.practicum.main.common.model.User;
import ru.practicum.main.common.repository.CommentRepository;
import ru.practicum.main.common.repository.EventRepository;
import ru.practicum.main.common.repository.UserRepository;
import ru.practicum.main.common.state.State;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PrivateCommentService {
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Transactional
    public CommentDTO addComment(Long userId, Long eventId, NewCommentDTO newCommentDTO) {
        User author = checkUser(userId);
        Event event = checkEvent(eventId);

        Comment comment = commentMapper.toComment(newCommentDTO, author, event);

        return commentMapper.toCommentDTO(commentRepository.save(comment));
    }

    public List<CommentShortDTO> getComments(Long userId, Long eventId, Pageable pageable) {
        checkUser(userId);

        if (eventId != null) {
            checkEvent(eventId);
            return commentRepository
                    .findCommentsByAuthorIdAndEventId(userId, eventId, pageable).stream()
                    .map(commentMapper::commentShortDTO)
                    .collect(Collectors.toList());
        } else {
            return commentRepository.findCommentsByAuthorId(userId, pageable).stream()
                    .map(commentMapper::commentShortDTO)
                    .collect(Collectors.toList());
        }
    }

    public CommentDTO getComment(Long userId, Long commentId) {
        checkUser(userId);
        Comment comment = checkComment(userId, commentId);

        return commentMapper.toCommentDTO(comment);
    }

    @Transactional
    public CommentDTO updateComment(Long userId, Long commentId, NewCommentDTO newCommentDTO) {
        checkUser(userId);

        Comment comment = checkComment(userId, commentId);
        comment.setText(newCommentDTO.getText());
        comment.setUpdatedOn(LocalDateTime.now());

        return commentMapper.toCommentDTO(comment);
    }

    @Transactional
    public void deleteComment(Long userId, Long commentId) {
        checkUser(userId);
        Comment comment = checkComment(userId, commentId);

        commentRepository.delete(comment);
    }

    private Comment checkComment(long userId, long commentId) {
        return commentRepository.findCommentByIdAndAuthorId(commentId, userId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Failed to comment with id=%s and user id=%s", commentId, userId)));
    }

    private User checkUser(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Failed to user with id=%s", userId)));
    }

    private Event checkEvent(long eventId) {
        return eventRepository.findEventByIdAndState(eventId, State.PUBLISHED)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Failed to event with id=%s", eventId)));
    }
}
