package ru.practicum.main.admin.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.main.admin.service.AdminCommentService;
import ru.practicum.main.common.CustomPageRequest;
import ru.practicum.main.common.dto.CommentDTO;
import ru.practicum.main.common.dto.NewCommentDTO;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/comments")
public class AdminCommentController {
    private static final Logger log = LoggerFactory.getLogger(AdminCommentController.class);
    private final AdminCommentService commentService;

    @GetMapping
    public List<CommentDTO> getComments(@RequestParam(required = false) Long userId,
                                        @RequestParam(required = false) Long eventId,
                                        @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                        @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Getting admin events");

        return commentService.getComments(userId, eventId, new CustomPageRequest(from, size, Sort.unsorted()));
    }

    @PatchMapping("/{commentId}")
    public CommentDTO updateComment(@PathVariable Long commentId,
                                    @Valid @RequestBody NewCommentDTO newCommentDTO) {
        log.info("Updating admin comment. Comment Id: {}, comment text: {}",
                commentId, newCommentDTO.getText());
        return commentService.updateComment(commentId, newCommentDTO);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeComment(@PathVariable Long commentId) {
        log.info("Removing admin comment. Comment id: {}", commentId);

        commentService.deleteComment(commentId);
    }
}
