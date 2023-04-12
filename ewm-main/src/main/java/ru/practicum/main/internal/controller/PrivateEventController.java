package ru.practicum.main.internal.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
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
import ru.practicum.main.common.dto.EventFullDTO;
import ru.practicum.main.common.dto.EventRequestStatusUpdateRequest;
import ru.practicum.main.common.dto.EventRequestStatusUpdateResult;
import ru.practicum.main.common.dto.EventShortDTO;
import ru.practicum.main.common.dto.NewEventDTO;
import ru.practicum.main.common.dto.ParticipationRequestDTO;
import ru.practicum.main.common.dto.UpdateEventUserRequest;
import ru.practicum.main.internal.service.PrivateEventService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/events")
public class PrivateEventController {
    private final Logger log = LoggerFactory.getLogger(PrivateEventController.class);
    private final PrivateEventService eventService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDTO createEvent(@PathVariable Long userId,
                                    @Valid @RequestBody NewEventDTO newEventDTO) {
        log.info("Creating event. User Id: {}, event: {}", userId, newEventDTO);

        return eventService.addEvent(userId, newEventDTO);
    }

    @GetMapping
    public List<EventShortDTO> getEvents(@PathVariable Long userId,
                                         @RequestParam(defaultValue = "0") Integer from,
                                         @RequestParam(defaultValue = "10") Integer size) {
        log.info("Getting events by current user. User Id: {}", userId);

        return eventService.getEvents(userId, new CustomPageRequest(from, size, Sort.unsorted()));
    }

    @GetMapping("/{eventId}")
    public EventFullDTO getEvent(@PathVariable Long userId,
                                 @PathVariable Long eventId) {
        log.info("Getting event by current user. User Id: {}, Event Id: {}", userId, eventId);

        return eventService.getEventById(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDTO updateEvent(@PathVariable Long userId,
                                    @PathVariable Long eventId,
                                    @RequestBody UpdateEventUserRequest updateEventUserRequest) {
        log.info("Updating event by current user. User Id: {}, Event Id: {}", userId, eventId);

        return eventService.updateEvent(userId, eventId, updateEventUserRequest);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDTO> getEventRequestsByCurrentUser(@PathVariable Long userId,
                                                                       @PathVariable Long eventId) {
        log.info("Getting event requests by current user Id: {}, event Id: {}", userId, eventId);

        return eventService.getEventRequests(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult changeStatus(@PathVariable Long userId,
                                                       @PathVariable Long eventId,
                                                       @RequestBody EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        log.info("Updating request status by user Id: {}, event Id: {}", userId, eventId);

        return eventService.updateStatusRequest(userId, eventId, eventRequestStatusUpdateRequest);
    }
}
