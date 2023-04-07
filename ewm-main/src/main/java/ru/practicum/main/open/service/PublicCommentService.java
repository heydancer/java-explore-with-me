package ru.practicum.main.open.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.common.dto.CommentDTO;
import ru.practicum.main.common.dto.CommentShortDTO;
import ru.practicum.main.common.exception.NotFoundException;
import ru.practicum.main.common.mapper.CommentMapper;
import ru.practicum.main.common.model.Comment;
import ru.practicum.main.common.model.Event;
import ru.practicum.main.common.repository.CommentRepository;
import ru.practicum.main.common.repository.EventRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PublicCommentService {
    private final EventRepository eventRepository;
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;

    public List<CommentShortDTO> getComments(Long eventId, Pageable pageable) {
        checkEvent(eventId);

        return commentRepository.findCommentsByEventId(eventId, pageable).stream()
                .map(commentMapper::commentShortDTO)
                .collect(Collectors.toList());
    }

    public CommentDTO getComment(Long commentId) {
        Comment comment = checkComment(commentId);
        return commentMapper.toCommentDTO(comment);
    }

    private Event checkEvent(long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Failed to find event with id=%s", eventId)));
    }

    private Comment checkComment(long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Failed to find comment with id=%s", commentId)));

    }
}
