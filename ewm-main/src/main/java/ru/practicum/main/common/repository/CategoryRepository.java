package ru.practicum.main.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main.common.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
