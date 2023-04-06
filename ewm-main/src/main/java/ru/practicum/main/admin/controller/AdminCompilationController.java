package ru.practicum.main.admin.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.main.admin.service.AdminCompilationService;
import ru.practicum.main.common.dto.CompilationDTO;
import ru.practicum.main.common.dto.NewCompilationDTO;
import ru.practicum.main.common.dto.UpdateCompilationRequest;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/compilations")
public class AdminCompilationController {
    private static final Logger log = LoggerFactory.getLogger(AdminCategoryController.class);
    private final AdminCompilationService compilationService;

    @Autowired
    public AdminCompilationController(AdminCompilationService compilationService) {
        this.compilationService = compilationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDTO createCompilation(@RequestBody @Valid NewCompilationDTO newCompilationDTO) {
        log.info("Creating compilation: {}", newCompilationDTO);

        return compilationService.addCompilation(newCompilationDTO);
    }

    @PatchMapping("/{compId}")
    public CompilationDTO updateCompilation(@PathVariable Long compId,
                                            @RequestBody UpdateCompilationRequest updateCompilationRequest) {
        log.info("Updating compilation. Id: {}, new compilation: {} ", compId, updateCompilationRequest);

        return compilationService.updateCompilation(compId, updateCompilationRequest);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeCompilation(@PathVariable Long compId) {
        log.info("Removing compilation. Id: {}", compId);

        compilationService.deleteById(compId);
    }
}
