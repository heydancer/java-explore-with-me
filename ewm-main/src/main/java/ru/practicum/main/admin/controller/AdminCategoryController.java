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
import ru.practicum.main.admin.service.AdminCategoryService;
import ru.practicum.main.common.dto.NewCategoryDTO;
import ru.practicum.main.common.dto.CategoryDTO;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/categories")
public class AdminCategoryController {
    private static final Logger log = LoggerFactory.getLogger(AdminCategoryController.class);
    private final AdminCategoryService categoryService;

    @Autowired
    public AdminCategoryController(AdminCategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDTO createCategory(@Valid @RequestBody NewCategoryDTO newCategoryDTO) {
        log.info("Creating category: {}", newCategoryDTO);

        return categoryService.addCategory(newCategoryDTO);
    }

    @PatchMapping("/{catId}")
    public CategoryDTO updateCategory(@PathVariable Long catId,
                                      @Valid @RequestBody NewCategoryDTO newCategoryDTO) {
        log.info("Updating category. Id: {}, new name: {} ", catId, newCategoryDTO.getName());

        return categoryService.updateCategory(catId, newCategoryDTO);
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeCategory(@PathVariable Long catId) {
        log.info("Removing category. Id: {}", catId);

        categoryService.deleteById(catId);
    }
}
