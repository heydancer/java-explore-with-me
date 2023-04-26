package ru.practicum.service.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.EndpointHitDTO;
import ru.practicum.dto.ViewStats;
import ru.practicum.service.service.StatsService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class StatsController {
    private static final Logger log = LoggerFactory.getLogger(StatsController.class);
    private final StatsService statsService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveInfo(@RequestBody EndpointHitDTO endpointHitDTO) {
        log.info("Saving information. APP:{}, URI:{}, IP:{}",
                endpointHitDTO.getApp(), endpointHitDTO.getUri(), endpointHitDTO.getIp());

        statsService.addEndpointHit(endpointHitDTO);
    }

    @GetMapping("/stats")
    public List<ViewStats> getStats(@RequestParam String start, @RequestParam String end,
                                    @RequestParam(required = false) List<String> uris,
                                    @RequestParam(defaultValue = "false") Boolean unique) {
        log.info("Getting stats. Start period: {}, end period: {}", start, end);

        return statsService.getStats(start, end, uris, unique);
    }
}
