package ru.practicum.main.internal.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.main.common.CustomPageRequest;
import ru.practicum.main.common.dto.CommentDTO;
import ru.practicum.main.common.dto.CommentShortDTO;
import ru.practicum.main.common.dto.NewCommentDTO;
import ru.practicum.main.internal.service.PrivateCommentService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/comments")
public class PrivateCommentController {
    private final Logger log = LoggerFactory.getLogger(PrivateCommentController.class);
    private final PrivateCommentService commentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDTO createComment(@PathVariable Long userId,
                                    @RequestParam Long eventId,
                                    @Valid @RequestBody NewCommentDTO newCommentDTO) {
        log.info("Creating comment. User Id: {}, event Id: {}, comment text: {}",
                userId, eventId, newCommentDTO.getText());

        return commentService.addComment(userId, eventId, newCommentDTO);
    }

    @GetMapping
    public List<CommentShortDTO> getComments(@PathVariable Long userId,
                                             @RequestParam(required = false) Long eventId,
                                             @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                             @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Getting comments by current user. User Id: {}, Event Id: {}", userId, eventId);

        return commentService.getComments(userId, eventId, new CustomPageRequest(from, size, Sort.unsorted()));
    }

    @GetMapping("/{commentId}")
    public CommentDTO getComment(@PathVariable Long userId,
                                 @PathVariable Long commentId) {
        log.info("Getting comment by current user. User Id: {}, comment Id: {}", userId, commentId);

        return commentService.getComment(userId, commentId);
    }

    @PatchMapping("/{commentId}")
    public CommentDTO updateComment(@PathVariable Long userId,
                                    @PathVariable Long commentId,
                                    @Valid @RequestBody NewCommentDTO newCommentDTO) {
        log.info("Updating comment. User Id: {}, comment Id: {}, comment text: {}",
                userId, commentId, newCommentDTO.getText());

        return commentService.updateComment(userId, commentId, newCommentDTO);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeComment(@PathVariable Long userId,
                              @PathVariable Long commentId) {
        log.info("Removing comment. User Id: {}, comment id: {}", userId, commentId);

        commentService.deleteComment(userId, commentId);
    }
}
