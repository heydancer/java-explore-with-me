package ru.practicum.main.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.common.CustomPageRequest;
import ru.practicum.main.common.dto.CategoryDTO;
import ru.practicum.main.common.dto.NewCategoryDTO;
import ru.practicum.main.common.exception.NotFoundException;
import ru.practicum.main.common.mapper.CategoryMapper;
import ru.practicum.main.common.model.Category;
import ru.practicum.main.common.repository.CategoryRepository;
import ru.practicum.main.common.repository.EventRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminCategoryService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final CategoryMapper categoryMapper;

    @Transactional
    public CategoryDTO addCategory(NewCategoryDTO newCategoryDTO) {
        Category category = categoryMapper.toCategory(newCategoryDTO);
        categoryRepository.save(category);

        return categoryMapper.toCategoryDTO(category);
    }

    @Transactional
    public CategoryDTO updateCategory(Long catId, NewCategoryDTO newCategoryDTO) {
        Category category = checkCategory(catId);
        category.setName(newCategoryDTO.getName());

        return categoryMapper.toCategoryDTO(category);
    }

    @Transactional
    public void deleteById(Long catId) {
        Category category = checkCategory(catId);

        if (!eventRepository.findFirstByOrderByCategoryAsc(catId,
                new CustomPageRequest(0, 1, Sort.unsorted())).isEmpty()) {
            throw new DataIntegrityViolationException("The category is not empty");
        }

        categoryRepository.delete(category);
    }

    private Category checkCategory(long catId) {
        return categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Category with id=%s was not found", catId)));
    }
}
