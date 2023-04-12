package ru.practicum.main.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.common.dto.CommentDTO;
import ru.practicum.main.common.dto.NewCommentDTO;
import ru.practicum.main.common.exception.NotFoundException;
import ru.practicum.main.common.mapper.CommentMapper;
import ru.practicum.main.common.model.Comment;
import ru.practicum.main.common.repository.CommentRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminCommentService {
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;

    public List<CommentDTO> getComments(Long userId, Long eventId, Pageable pageable) {
        return commentRepository.findAdminComments(userId, eventId, pageable).stream()
                .map(commentMapper::toCommentDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public CommentDTO updateComment(Long commentId, NewCommentDTO newCommentDTO) {
        Comment comment = checkComment(commentId);
        comment.setText(newCommentDTO.getText());
        comment.setUpdatedOn(LocalDateTime.now());

        return commentMapper.toCommentDTO(comment);
    }

    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = checkComment(commentId);

        commentRepository.delete(comment);
    }

    private Comment checkComment(long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Failed to comment with id=%s", commentId)));
    }
}
