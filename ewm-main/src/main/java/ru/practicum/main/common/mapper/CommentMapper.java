package ru.practicum.main.common.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.main.common.dto.CommentDTO;
import ru.practicum.main.common.dto.CommentShortDTO;
import ru.practicum.main.common.dto.NewCommentDTO;
import ru.practicum.main.common.model.Comment;
import ru.practicum.main.common.model.Event;
import ru.practicum.main.common.model.User;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class CommentMapper {
    private final UserMapper userMapper;

    public CommentDTO toCommentDTO(Comment comment) {
        return CommentDTO.builder()
                .id(comment.getId())
                .text(comment.getText())
                .user(userMapper.toUserShortDTO(comment.getAuthor()))
                .eventId(comment.getEvent().getId())
                .createdOn(comment.getCreatedOn())
                .updatedOn(comment.getUpdatedOn())
                .build();
    }

    public CommentShortDTO commentShortDTO(Comment comment) {
        return CommentShortDTO.builder()
                .text(comment.getText())
                .createdOn(comment.getCreatedOn())
                .build();
    }

    public Comment toComment(NewCommentDTO newCommentDTO, User author, Event event) {
        return Comment.builder()
                .text(newCommentDTO.getText())
                .author(author)
                .event(event)
                .createdOn(LocalDateTime.now())
                .build();
    }
}
