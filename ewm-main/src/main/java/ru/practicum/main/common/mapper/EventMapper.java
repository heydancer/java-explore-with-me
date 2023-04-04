package ru.practicum.main.common.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.main.common.dto.EventFullDTO;
import ru.practicum.main.common.dto.EventShortDTO;
import ru.practicum.main.common.dto.NewEventDTO;
import ru.practicum.main.common.model.Event;

import java.time.LocalDateTime;

@Component
public class EventMapper {
    private final UserMapper userMapper;
    private final CategoryMapper categoryMapper;

    @Autowired
    public EventMapper(UserMapper userMapper, CategoryMapper categoryMapper) {
        this.userMapper = userMapper;
        this.categoryMapper = categoryMapper;
    }

    public EventFullDTO toEventFullDTO(Event event) {
        return EventFullDTO.builder()
                .id(event.getId())
                .confirmedRequests(event.getConfirmedRequests())
                .category(event.getCategory())
                .eventDate(event.getEventDate())
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .description(event.getDescription())
                .initiator(event.getInitiator())
                .createdOn(event.getCreatedOn())
                .paid(event.getPaid())
                .state(event.getState())
                .location(event.getLocation())
                .publishedOn(event.getPublishedOn())
                .participantLimit(event.getParticipantLimit())
                .requestModeration(event.getRequestModeration())
                .build();
    }

    public EventShortDTO toEventShortDTO(Event event) {
        return EventShortDTO.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(categoryMapper.toCategoryDTO(event.getCategory()))
                .initiator(userMapper.toUserShortDTO(event.getInitiator()))
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(event.getEventDate())
                .paid(event.getPaid())
                .title(event.getTitle())
                .build();
    }

    public Event toEvent(NewEventDTO newEventDTO) {
        return Event.builder()
                .eventDate(newEventDTO.getEventDate())
                .title(newEventDTO.getTitle())
                .annotation(newEventDTO.getAnnotation())
                .description(newEventDTO.getDescription())
                .paid(newEventDTO.getPaid())
                .requestModeration(newEventDTO.getRequestModeration())
                .participantLimit(newEventDTO.getParticipantLimit())
                .location(newEventDTO.getLocation())
                .createdOn(LocalDateTime.now())
                .build();
    }
}
