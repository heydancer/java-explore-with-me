package ru.practicum.main.internal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.common.dto.EventFullDTO;
import ru.practicum.main.common.dto.EventRequestStatusUpdateRequest;
import ru.practicum.main.common.dto.EventRequestStatusUpdateResult;
import ru.practicum.main.common.dto.EventShortDTO;
import ru.practicum.main.common.dto.NewEventDTO;
import ru.practicum.main.common.dto.ParticipationRequestDTO;
import ru.practicum.main.common.dto.UpdateEventUserRequest;
import ru.practicum.main.common.exception.ForbiddenException;
import ru.practicum.main.common.exception.NotFoundException;
import ru.practicum.main.common.mapper.EventMapper;
import ru.practicum.main.common.model.Category;
import ru.practicum.main.common.model.Event;
import ru.practicum.main.common.model.User;
import ru.practicum.main.common.repository.CategoryRepository;
import ru.practicum.main.common.repository.EventRepository;
import ru.practicum.main.common.repository.RequestRepository;
import ru.practicum.main.common.repository.UserRepository;
import ru.practicum.main.common.mapper.RequestMapper;
import ru.practicum.main.common.model.Request;
import ru.practicum.main.common.state.RequestStatus;
import ru.practicum.main.common.state.State;
import ru.practicum.main.common.state.UserStateAction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class PrivateEventService {
    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final EventMapper eventMapper;
    private final RequestMapper requestsMapper;

    @Autowired
    public PrivateEventService(RequestRepository requestRepository,
                               EventRepository eventRepository,
                               UserRepository userRepository,
                               CategoryRepository categoryRepository,
                               EventMapper eventMapper,
                               RequestMapper requestsMapper) {
        this.requestRepository = requestRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.eventMapper = eventMapper;
        this.requestsMapper = requestsMapper;
    }

    @Transactional
    public EventFullDTO addEvent(Long userId, NewEventDTO newEventDTO) {
        User user = checkUser(userId);
        Category category = checkCategory(newEventDTO.getCategory());

        checkEventDate(newEventDTO.getEventDate());

        Event event = eventMapper.toEvent(newEventDTO);
        event.setInitiator(user);
        event.setCategory(category);
        event.setState(State.PENDING);

        EventFullDTO eventFullDTO = eventMapper.toEventFullDTO(eventRepository.save(event));
        eventFullDTO.setViews(0L);

        return eventFullDTO;
    }

    public EventFullDTO getEventById(Long userId, Long eventId) {
        Event event = checkEventOwner(eventId, userId);

        return eventMapper.toEventFullDTO(event);
    }

    public List<EventShortDTO> getEvents(Long userId, Pageable pageable) {
        checkUser(userId);

        List<Event> events = eventRepository.findEventsByInitiatorId(userId, pageable);

        return events.stream()
                .map(eventMapper::toEventShortDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public EventFullDTO updateEvent(Long userId, Long eventId,
                                    UpdateEventUserRequest updateEventUserRequest) {
        Event event = checkEventOwner(eventId, userId);
        checkEventDate(updateEventUserRequest.getEventDate());

        if (event.getState().equals(State.PUBLISHED)) {
            throw new ForbiddenException("It is forbidden to change published events");
        }

        if (updateEventUserRequest.getStateAction() != null) {
            if (updateEventUserRequest.getStateAction() == UserStateAction.SEND_TO_REVIEW) {
                event.setState(State.PENDING);
            } else if (updateEventUserRequest.getStateAction() == UserStateAction.CANCEL_REVIEW) {
                event.setState(State.CANCELED);
            }
        }

        if (updateEventUserRequest.getTitle() != null) {
            event.setTitle(updateEventUserRequest.getTitle());
        }

        if (updateEventUserRequest.getAnnotation() != null) {
            event.setAnnotation(updateEventUserRequest.getAnnotation());
        }

        if (updateEventUserRequest.getDescription() != null) {
            event.setDescription(updateEventUserRequest.getDescription());
        }

        if (updateEventUserRequest.getCategory() != null) {
            Category category = checkCategory(updateEventUserRequest.getCategory());
            event.setCategory(category);
        }

        if (updateEventUserRequest.getEventDate() != null) {
            event.setEventDate(updateEventUserRequest.getEventDate());
        }

        if (updateEventUserRequest.getLocation() != null) {
            event.setLocation(updateEventUserRequest.getLocation());
        }

        if (updateEventUserRequest.getPaid() != null) {
            event.setPaid(updateEventUserRequest.getPaid());
        }

        if (updateEventUserRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventUserRequest.getParticipantLimit());
        }

        if (updateEventUserRequest.getRequestModeration() != null) {
            event.setRequestModeration(updateEventUserRequest.getRequestModeration());
        }

        return eventMapper.toEventFullDTO(event);
    }

    public List<ParticipationRequestDTO> getEventRequests(Long userId, Long eventId) {
        checkEventOwner(eventId, userId);

        return requestRepository.findRequestsByEventId(eventId).stream()
                .map(requestsMapper::toParticipationRequestDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public EventRequestStatusUpdateResult updateStatusRequest(Long userId, Long eventId,
                                                              EventRequestStatusUpdateRequest eventRequest) {
        Event event = checkEventOwner(eventId, userId);
        checkModerationAndParticipantLimit(event.getRequestModeration(), event.getParticipantLimit());

        EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult(
                new ArrayList<>(), new ArrayList<>());

        Integer confirmedRequests = requestRepository
                .findByEventIdConfirmed(eventId).size();

        List<Request> requests = requestRepository
                .findByEventIdAndRequestsIds(eventId, eventRequest.getRequestIds());

        if (eventRequest.getStatus().equals(RequestStatus.CONFIRMED)) {
            if (eventRequest.getRequestIds().size() <= (event.getParticipantLimit() - confirmedRequests)) {
                requests.forEach(request -> request.setStatus(RequestStatus.CONFIRMED));

                List<ParticipationRequestDTO> requestDTO = requests.stream()
                        .map(requestsMapper::toParticipationRequestDTO)
                        .collect(Collectors.toList());

                result.setConfirmedRequests(requestDTO);

            } else if ((confirmedRequests + requests.size()) > event.getParticipantLimit()) {
                requests.forEach(request -> request.setStatus(RequestStatus.REJECTED));

                List<ParticipationRequestDTO> requestDTO = requests.stream()
                        .map(requestsMapper::toParticipationRequestDTO)
                        .collect(Collectors.toList());

                result.setRejectedRequests(requestDTO);

                throw new ForbiddenException("Requests limit exceeded");
            }

        } else if (eventRequest.getStatus().equals(RequestStatus.REJECTED)) {
            for (Request request : requests) {
                if (request.getStatus().equals(RequestStatus.CONFIRMED)) {
                    throw new ForbiddenException("You can't reject confirmed request");
                }

                request.setStatus(RequestStatus.REJECTED);
            }

            List<ParticipationRequestDTO> requestDto = requests.stream()
                    .map(requestsMapper::toParticipationRequestDTO)
                    .collect(Collectors.toList());

            result.setRejectedRequests(requestDto);
        }

        return result;
    }

    private void checkEventDate(LocalDateTime eventDate) {
        if (eventDate != null && eventDate.isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ForbiddenException(
                    String.format("Field: eventDate. Error: must contain a date that has not yet arrived. " +
                            "Value: %s", eventDate));
        }
    }

    private void checkModerationAndParticipantLimit(boolean moderation, long limit) {
        if (!moderation || limit == 0) {
            throw new ForbiddenException("Confirmation is not required");
        }
    }

    private User checkUser(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Failed to find user with id=%s", userId)));
    }

    private Category checkCategory(long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Failed to find category with id=%s", categoryId)));
    }

    private Event checkEventOwner(long eventId, long userId) {
        return eventRepository.findEventByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Failed to find event with id=%s, user id=%s", eventId, userId)));
    }

}
