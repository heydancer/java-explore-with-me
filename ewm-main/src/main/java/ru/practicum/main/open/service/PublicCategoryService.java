package ru.practicum.main.open.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.common.dto.CategoryDTO;
import ru.practicum.main.common.exception.NotFoundException;
import ru.practicum.main.common.mapper.CategoryMapper;
import ru.practicum.main.common.model.Category;
import ru.practicum.main.common.repository.CategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class PublicCategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Autowired
    public PublicCategoryService(CategoryRepository categoryRepository,
                                 CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    public List<CategoryDTO> getCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable).stream()
                .map(categoryMapper::toCategoryDTO)
                .collect(Collectors.toList());
    }

    public CategoryDTO getCategoryById(Long catId) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Failed to find category with id=%d", catId)));

        return categoryMapper.toCategoryDTO(category);
    }
}
