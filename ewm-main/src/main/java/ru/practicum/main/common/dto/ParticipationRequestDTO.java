package ru.practicum.main.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.main.common.state.RequestStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParticipationRequestDTO {
    private Long id;
    private Long event;
    private LocalDateTime created;
    private Long requester;
    private RequestStatus status;
}
