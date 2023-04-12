package ru.practicum.main.open.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.main.common.CustomPageRequest;
import ru.practicum.main.common.dto.CommentDTO;
import ru.practicum.main.common.dto.CommentShortDTO;
import ru.practicum.main.internal.controller.PrivateCommentController;
import ru.practicum.main.open.service.PublicCommentService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class PublicCommentController {
    private final Logger log = LoggerFactory.getLogger(PrivateCommentController.class);
    private final PublicCommentService commentService;

    @GetMapping
    public List<CommentShortDTO> getComments(@RequestParam Long eventId,
                                             @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                             @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Getting public comments. Event Id: {}", eventId);

        return commentService.getComments(eventId, new CustomPageRequest(from, size, Sort.unsorted()));
    }

    @GetMapping("/{commentId}")
    public CommentDTO getComment(@PathVariable Long commentId) {
        log.info("Getting public comment. commentId Id: {}", commentId);

        return commentService.getComment(commentId);
    }
}
