package ru.practicum.main.admin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.common.dto.NewUserRequest;
import ru.practicum.main.common.dto.UserDTO;
import ru.practicum.main.common.exception.NotFoundException;
import ru.practicum.main.common.mapper.UserMapper;
import ru.practicum.main.common.model.User;
import ru.practicum.main.common.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class AdminUserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public AdminUserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Transactional
    public UserDTO addUser(NewUserRequest newUserRequest) {
        User user = userMapper.toUser(newUserRequest);
        return userMapper.toUserDTO(userRepository.save(user));
    }

    public List<UserDTO> getUsers(List<Long> ids, Pageable pageable) {
        if (!ids.isEmpty()) {
            return userRepository.findUsersByIdIn(ids, pageable).stream()
                    .map(userMapper::toUserDTO)
                    .collect(Collectors.toList());
        } else {
            return userRepository.findAll(pageable).stream()
                    .map(userMapper::toUserDTO)
                    .collect(Collectors.toList());
        }
    }

    @Transactional
    public void deleteUserById(Long userId) {
        User user = checkUser(userId);
        userRepository.delete(user);
    }

    private User checkUser(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("User with id=%s was not found", userId)));
    }
}
