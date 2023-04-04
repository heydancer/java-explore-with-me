package ru.practicum.main.admin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.common.dto.EventFullDTO;
import ru.practicum.main.common.dto.UpdateEventAdminRequest;
import ru.practicum.main.common.exception.ForbiddenException;
import ru.practicum.main.common.exception.NotFoundException;
import ru.practicum.main.common.mapper.EventMapper;
import ru.practicum.main.common.state.AdminStateAction;
import ru.practicum.main.common.model.Category;
import ru.practicum.main.common.model.Event;
import ru.practicum.main.common.state.State;
import ru.practicum.main.common.repository.CategoryRepository;
import ru.practicum.main.common.repository.EventRepository;
import ru.practicum.service.utils.DateFormatter;


import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class AdminEventService {
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final EventMapper eventMapper;

    @Autowired
    public AdminEventService(EventRepository eventRepository,
                             CategoryRepository categoryRepository,
                             EventMapper eventMapper) {
        this.eventRepository = eventRepository;
        this.categoryRepository = categoryRepository;
        this.eventMapper = eventMapper;
    }

    public List<EventFullDTO> getAdminEvents(List<Long> users, List<State> states, List<Long> categories,
                                             String rangeStart, String rangeEnd, Pageable pageable) {

        LocalDateTime start = rangeStart != null ? DateFormatter.toTime(rangeStart) : null;
        LocalDateTime end = rangeEnd != null ? DateFormatter.toTime(rangeEnd) : null;

        checkDateTimePeriod(start, end);

        List<Event> events = eventRepository.findAdminEvents(users, states, categories, start, end, pageable);

        return events.stream()
                .map(eventMapper::toEventFullDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public EventFullDTO updateAdminEvent(Long eventId, UpdateEventAdminRequest eventAdminRequest) {
        Event event = checkEvent(eventId);

        if (eventAdminRequest.getTitle() != null) {
            event.setTitle(eventAdminRequest.getTitle());
        }

        if (eventAdminRequest.getAnnotation() != null) {
            event.setAnnotation(eventAdminRequest.getAnnotation());
        }

        if (eventAdminRequest.getCategory() != null) {
            Category category = checkCategory(eventAdminRequest.getCategory());
            event.setCategory(category);
        }

        if (eventAdminRequest.getDescription() != null) {
            event.setDescription(eventAdminRequest.getDescription());
        }

        if (eventAdminRequest.getEventDate() != null) {
            if (eventAdminRequest.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
                throw new ForbiddenException("The start date of the event must be no earlier " +
                        "than one hour from the date of publication");
            } else {
                event.setEventDate(eventAdminRequest.getEventDate());
            }
        }

        if (eventAdminRequest.getLocation() != null) {
            event.setLocation(eventAdminRequest.getLocation());
        }

        if (eventAdminRequest.getPaid() != null) {
            event.setPaid(eventAdminRequest.getPaid());
        }

        if (eventAdminRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(eventAdminRequest.getParticipantLimit());
        }

        if (eventAdminRequest.getRequestModeration() != null) {
            event.setRequestModeration(eventAdminRequest.getRequestModeration());
        }

        if (eventAdminRequest.getStateAction() != null) {
            if (!event.getState().equals(State.PENDING)) {
                throw new ForbiddenException(
                        String.format("Cannot publish the event because it's not in the right state: %s",
                                event.getState()));
            }

            if (eventAdminRequest.getStateAction() == AdminStateAction.PUBLISH_EVENT) {
                event.setState(State.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
            } else if (eventAdminRequest.getStateAction() == AdminStateAction.REJECT_EVENT) {
                event.setState(State.REJECTED);
            }
        }

        return eventMapper.toEventFullDTO(event);
    }

    private Event checkEvent(long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Failed to find event with id=%d", eventId)));
    }

    private Category checkCategory(long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Failed to find category with id=%d", categoryId)));
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
}
