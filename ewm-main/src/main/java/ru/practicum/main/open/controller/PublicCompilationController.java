package ru.practicum.main.open.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.main.common.CustomPageRequest;
import ru.practicum.main.common.dto.CompilationDTO;
import ru.practicum.main.open.service.PublicCompilationService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/compilations")
public class PublicCompilationController {
    private static final Logger log = LoggerFactory.getLogger(PublicCompilationController.class);
    private final PublicCompilationService compilationService;

    @GetMapping
    public List<CompilationDTO> getCompilations(@RequestParam(required = false) Boolean pinned,
                                                @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Getting compilations");
        return compilationService.getCompilations(pinned, new CustomPageRequest(from, size, Sort.unsorted()));
    }

    @GetMapping("/{compId}")
    public CompilationDTO getCompilation(@PathVariable Long compId) {
        log.info("Getting compilation. Compilation Id: {}", compId);

        return compilationService.getCompilationById(compId);
    }
}
