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
import ru.practicum.main.common.dto.CategoryDTO;
import ru.practicum.main.open.service.PublicCategoryService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class PublicCategoryController {
    private static final Logger log = LoggerFactory.getLogger(PublicCategoryController.class);
    private final PublicCategoryService categoryService;

    @GetMapping
    public List<CategoryDTO> getCategories(@RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                           @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Getting category");

        return categoryService.getCategories(new CustomPageRequest(from, size, Sort.unsorted()));
    }

    @GetMapping("/{catId}")
    CategoryDTO getCategory(@PathVariable Long catId) {
        log.info("Getting category. Category Id: {}", catId);

        return categoryService.getCategoryById(catId);
    }
}
