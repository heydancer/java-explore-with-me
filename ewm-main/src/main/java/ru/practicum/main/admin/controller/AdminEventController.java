package ru.practicum.main.admin.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.main.admin.service.AdminEventService;
import ru.practicum.main.common.CustomPageRequest;
import ru.practicum.main.common.dto.EventFullDTO;
import ru.practicum.main.common.dto.UpdateEventAdminRequest;
import ru.practicum.main.common.state.State;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/events")
public class AdminEventController {
    private static final Logger log = LoggerFactory.getLogger(AdminEventController.class);
    private final AdminEventService eventService;

    @GetMapping
    public List<EventFullDTO> getEvents(@RequestParam(required = false) List<Long> users,
                                        @RequestParam(required = false) List<State> states,
                                        @RequestParam(required = false) List<Long> categories,
                                        @RequestParam(required = false) String rangeStart,
                                        @RequestParam(required = false) String rangeEnd,
                                        @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                        @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Getting admin events");

        return eventService.getAdminEvents(users, states, categories, rangeStart, rangeEnd,
                new CustomPageRequest(from, size, Sort.unsorted()));
    }

    @PatchMapping("/{eventId}")
    public EventFullDTO updateEvent(@PathVariable Long eventId,
                                    @RequestBody UpdateEventAdminRequest event) {
        log.info("Updating admin events. Event id: {}, event: {}", eventId, event);

        return eventService.updateAdminEvent(eventId, event);
    }
}
