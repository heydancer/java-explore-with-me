package ru.practicum.service.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.service.model.EndpointHit;
import ru.practicum.dto.EndpointHitDTO;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Component
public class EndpointHitMapper  {
    public EndpointHit toModel(EndpointHitDTO endpointHitDTO) {
        return EndpointHit.builder()
                .id(endpointHitDTO.getId())
                .app(endpointHitDTO.getApp())
                .uri(endpointHitDTO.getUri())
                .ip(endpointHitDTO.getIp())
                .timestamp(endpointHitDTO.getTimestamp())
                .build();
    }

    public EndpointHitDTO toDTO(String api, HttpServletRequest request) {
        return EndpointHitDTO.builder()
                .ip(request.getRemoteAddr())
                .app(api)
                .uri(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
    }
}
