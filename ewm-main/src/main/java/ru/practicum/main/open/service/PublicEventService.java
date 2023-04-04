package ru.practicum.main.open.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.client.StatsClient;
import ru.practicum.dto.ViewStats;
import ru.practicum.main.common.CustomPageRequest;
import ru.practicum.main.common.dto.EventFullDTO;
import ru.practicum.main.common.dto.EventShortDTO;
import ru.practicum.main.common.exception.ForbiddenException;
import ru.practicum.main.common.exception.NotFoundException;
import ru.practicum.main.common.mapper.EventMapper;
import ru.practicum.main.common.model.Event;
import ru.practicum.main.common.repository.EventRepository;
import ru.practicum.main.common.state.EventSort;
import ru.practicum.main.common.state.State;
import ru.practicum.service.mapper.EndpointHitMapper;
import ru.practicum.service.utils.DateFormatter;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class PublicEventService {
    private final EndpointHitMapper hitMapper;
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final StatsClient statsClient;
    @Value(value = "${app.name}")
    private String appName;

    @Autowired
    public PublicEventService(
            EndpointHitMapper hitMapper,
            EventRepository eventRepository,
            EventMapper eventMapper,
            StatsClient statsClient) {
        this.hitMapper = hitMapper;
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
        this.statsClient = statsClient;
    }

    public EventFullDTO getPublicEvent(Long eventId, HttpServletRequest request) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Failed to find event with id=%d", eventId)));

        if (!event.getState().equals(State.PUBLISHED)) {
            throw new NotFoundException(
                    String.format("Event with id: %s is not published", eventId));
        }

        statsClient.addHit(hitMapper.toDTO(appName, request));

        EventFullDTO fullDTO = eventMapper.toEventFullDTO(event);

        List<ViewStats> viewStats = getViews(Collections.singletonList(event));
        fullDTO.setViews(viewStats.get(0).getHits());

        return fullDTO;
    }

    public List<EventShortDTO> getPublicEvents(String text, List<Long> categories, Boolean paid,
                                               String rangeStart, String rangeEnd, Boolean onlyAvailable,
                                               EventSort sort, Integer from, Integer size, HttpServletRequest request) {

        LocalDateTime start = rangeStart != null ? DateFormatter.toTime(rangeStart) : null;
        LocalDateTime end = rangeEnd != null ? DateFormatter.toTime(rangeEnd) : null;

        checkDateTimePeriod(start, end);

        Pageable pageable = createPageableWithSort(from, size, sort);

        List<Event> events = eventRepository.findPublicEvents(text.toLowerCase(), categories, paid,
                start, end, State.PUBLISHED, pageable);

        statsClient.addHit(hitMapper.toDTO(appName, request));

        List<EventShortDTO> shortDTOS = events.stream()
                .map(eventMapper::toEventShortDTO)
                .collect(Collectors.toList());

        if (onlyAvailable) {
            shortDTOS = events.stream()
                    .filter(e -> e.getConfirmedRequests() < e.getParticipantLimit())
                    .map(eventMapper::toEventShortDTO)
                    .collect(Collectors.toList());
        }

        Map<Long, Long> views = new HashMap<>();

        List<ViewStats> viewStats = getViews(events);
        viewStats.forEach(stat -> {
            Long eventId = Long.parseLong(stat.getUri()
                    .split("/", 0)[2]);
            views.put(eventId, views.getOrDefault(eventId, 0L) + stat.getHits());
        });

        shortDTOS.forEach(shortDTO -> shortDTO.setViews(views.get(shortDTO.getId())));

        return shortDTOS;
    }

    private Pageable createPageableWithSort(int from, int size, EventSort sort) {
        if (sort == EventSort.VIEWS) {
            return new CustomPageRequest(from, size, Sort.by("views"));
        } else if (sort == EventSort.EVENT_DATE) {
            return new CustomPageRequest(from, size, Sort.by("eventDate"));
        } else {
            return new CustomPageRequest(from, size, Sort.unsorted());
        }
    }

    private void checkDateTimePeriod(LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        if (rangeStart != null && rangeEnd != null) {
            if (rangeStart.isAfter(rangeEnd)) {
                throw new ForbiddenException(
                        String.format("Start date: %s of the interval must be earlier than the end: %s date",
                                rangeStart, rangeEnd));
            }
        }
    }

    private List<ViewStats> getViews(List<Event> events) {
        String min = "2000-01-01 00:00:00";
        String max = "3000-01-01 00:00:00";

        String[] encoderTimes = DateFormatter.encoder(min, max);

        List<String> uris = events.stream()
                .map(event -> "/events/" + event.getId())
                .collect(Collectors.toList());

        return statsClient.getStats(encoderTimes[0], encoderTimes[1], uris, false);
    }
}
