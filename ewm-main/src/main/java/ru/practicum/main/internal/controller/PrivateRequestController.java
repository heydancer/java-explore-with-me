package ru.practicum.main.internal.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.main.common.dto.ParticipationRequestDTO;
import ru.practicum.main.internal.service.PrivateRequestService;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/requests")
public class PrivateRequestController {
    private final Logger log = LoggerFactory.getLogger(PrivateRequestController.class);
    private final PrivateRequestService requestService;

    @Autowired
    public PrivateRequestController(PrivateRequestService requestService) {
        this.requestService = requestService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ParticipationRequestDTO createRequest(@PathVariable Long userId,
                                          @RequestParam Long eventId) {
        log.info("Creating request. User Id: {}, event Id: {}", userId, eventId);

        return requestService.addRequest(userId, eventId);
    }

    @GetMapping
    List<ParticipationRequestDTO> getRequests(@PathVariable Long userId) {
        log.info("Getting requests. User Id: {}", userId);

        return requestService.getRequests(userId);
    }

    @PatchMapping("/{requestId}/cancel")
    ParticipationRequestDTO canselRequest(@PathVariable Long userId,
                                          @PathVariable Long requestId) {
        log.info("Cancel request. User Id: {}, request Id: {}", userId, requestId);

        return requestService.canselRequest(userId, requestId);
    }
}
