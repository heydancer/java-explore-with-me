package ru.practicum.main.common.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.main.common.dto.ParticipationRequestDTO;
import ru.practicum.main.common.model.Request;

@Component
public class RequestMapper {
    public ParticipationRequestDTO toParticipationRequestDTO(Request request) {
        return ParticipationRequestDTO.builder()
                .id(request.getId())
                .requester(request.getRequester().getId())
                .created(request.getCreated())
                .event(request.getEvent().getId())
                .status(request.getStatus())
                .build();
    }
}
