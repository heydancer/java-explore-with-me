package ru.practicum.main.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    private Long id;
    private String text;
    private UserShortDTO user;
    private Long eventId;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
}
