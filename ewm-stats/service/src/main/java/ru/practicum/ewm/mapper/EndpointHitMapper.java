package ru.practicum.ewm.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.dto.EndpointHitDTO;
import ru.practicum.ewm.model.EndpointHit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class EndpointHitMapper implements BaseMapper<EndpointHitDTO, EndpointHit> {
    private static final DateTimeFormatter formatter = DateTimeFormatter
            .ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public EndpointHit toModel(EndpointHitDTO endpointHitDTO) {
        return EndpointHit.builder()
                .id(endpointHitDTO.getId())
                .app(endpointHitDTO.getApp())
                .uri(endpointHitDTO.getUri())
                .ip(endpointHitDTO.getIp())
                .timestamp(LocalDateTime.parse(endpointHitDTO.getTimestamp(), formatter))
                .build();
    }

    @Override
    public EndpointHitDTO toDTO(EndpointHit endpointHit) {
        return EndpointHitDTO.builder()
                .id(endpointHit.getId())
                .app(endpointHit.getApp())
                .uri(endpointHit.getUri())
                .build();
    }
}
