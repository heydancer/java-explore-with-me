package ru.practicum.main.common.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.main.common.dto.NewUserRequest;
import ru.practicum.main.common.dto.UserDTO;
import ru.practicum.main.common.dto.UserShortDTO;
import ru.practicum.main.common.model.User;

@Component
public class UserMapper {

    public UserDTO toUserDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public UserShortDTO toUserShortDTO(User user) {
        return UserShortDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }

    public User toUser(NewUserRequest newUserRequest) {
        return User.builder()
                .name(newUserRequest.getName())
                .email(newUserRequest.getEmail())
                .build();
    }
}
