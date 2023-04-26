package ru.practicum.main.common.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.main.common.dto.CategoryDTO;
import ru.practicum.main.common.dto.NewCategoryDTO;
import ru.practicum.main.common.model.Category;

@Component
public class CategoryMapper {
    public CategoryDTO toCategoryDTO(Category category) {
        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public Category toCategory(NewCategoryDTO newCategoryDTO) {
        return Category.builder()
                .name(newCategoryDTO.getName())
                .build();
    }
}
