package ru.practicum.main.open.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.main.common.dto.EventFullDTO;
import ru.practicum.main.common.dto.EventShortDTO;
import ru.practicum.main.common.state.EventSort;
import ru.practicum.main.open.service.PublicEventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class PublicEventController {
    private final Logger log = LoggerFactory.getLogger(PublicEventController.class);
    private final PublicEventService eventService;

    @GetMapping("/{id}")
    EventFullDTO getEvent(HttpServletRequest request, @PathVariable Long id) {
        log.info("Getting public event by id: {}", id);

        return eventService.getPublicEvent(id, request);
    }

    @GetMapping
    List<EventShortDTO> getEvents(HttpServletRequest request,
                                  @RequestParam(required = false) String text,
                                  @RequestParam(required = false) List<Long> categories,
                                  @RequestParam(required = false) Boolean paid,
                                  @RequestParam(required = false) String rangeStart,
                                  @RequestParam(required = false) String rangeEnd,
                                  @RequestParam(required = false, defaultValue = "false") Boolean onlyAvailable,
                                  @RequestParam(required = false) EventSort sort,
                                  @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
                                  @RequestParam(required = false, defaultValue = "10") @Positive Integer size) {
        log.info("Getting public events");

        return eventService.getPublicEvents(text, categories, paid,
                rangeStart, rangeEnd, onlyAvailable, sort, from, size, request);
    }
}
