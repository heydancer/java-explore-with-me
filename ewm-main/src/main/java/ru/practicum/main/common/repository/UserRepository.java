package ru.practicum.main.common.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main.common.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findUsersByIdIn(List<Long> userIds, Pageable pageable);
}
