package ru.practicum.main.internal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.common.dto.ParticipationRequestDTO;
import ru.practicum.main.common.exception.ForbiddenException;
import ru.practicum.main.common.exception.NotFoundException;
import ru.practicum.main.common.model.Event;
import ru.practicum.main.common.model.User;
import ru.practicum.main.common.repository.EventRepository;
import ru.practicum.main.common.repository.RequestRepository;
import ru.practicum.main.common.repository.UserRepository;
import ru.practicum.main.common.mapper.RequestMapper;
import ru.practicum.main.common.model.Request;
import ru.practicum.main.common.state.RequestStatus;
import ru.practicum.main.common.state.State;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class PrivateRequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RequestMapper requestsMapper;

    @Autowired
    public PrivateRequestService(RequestRepository requestRepository, UserRepository userRepository, EventRepository eventRepository, RequestMapper requestsMapper) {
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.requestsMapper = requestsMapper;
    }

    @Transactional
    public ParticipationRequestDTO addRequest(Long userId, Long eventId) {
        User user = checkUser(userId);
        Event event = checkEvent(eventId);

        RequestStatus status;

        Integer confirmedRequests = requestRepository
                .findByEventIdAndStatus(eventId, RequestStatus.CONFIRMED).size();

        requestRepository.findByEventAndRequesterId(eventId, userId).ifPresent(request -> {
            throw new ForbiddenException("Event request already created");
        });

        if (userId.equals(event.getInitiator().getId())) {
            throw new ForbiddenException("You can't leave a request for your event");
        }

        if (event.getState() != State.PUBLISHED) {
            throw new ForbiddenException("You can't participate in an unpublished event");
        }

        if ((event.getParticipantLimit() != 0) && (event.getParticipantLimit() <= confirmedRequests)) {
            throw new ForbiddenException("Membership limit exceeded");
        }

        if (!event.getRequestModeration() || (event.getParticipantLimit() == 0)) {
            status = RequestStatus.CONFIRMED;
        } else {
            status = RequestStatus.PENDING;
        }

        Request request = Request.builder()
                .created(LocalDateTime.now())
                .event(event)
                .requester(user)
                .status(status)
                .build();

        return requestsMapper.toParticipationRequestDTO(requestRepository.save(request));
    }

    public List<ParticipationRequestDTO> getRequests(Long userId) {
        return requestRepository.findByRequesterId(userId).stream()
                .map(requestsMapper::toParticipationRequestDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ParticipationRequestDTO canselRequest(Long userId, Long requestId) {
        Request request = requestRepository.findByIdAndRequesterId(requestId, userId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Failed to request. Request id=%s. Requester id=%s", requestId, userId)));

        request.setStatus(RequestStatus.CANCELED);

        return requestsMapper.toParticipationRequestDTO(request);
    }

    private User checkUser(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Failed to user with id=%s", userId)));
    }

    private Event checkEvent(long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Failed to event with id=%s", eventId)));
    }
}
