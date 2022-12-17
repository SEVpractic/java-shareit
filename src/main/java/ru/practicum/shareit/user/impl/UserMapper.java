package ru.practicum.shareit.user.impl;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

public class UserMapper {
    private UserMapper() {
    }

    public static User toItem(UserDto userDto) {
        User user = User.builder()
                .email(userDto.getEmail())
                .name(userDto.getName())
                .build();

        return userDto.getId() == null ? user : user.toBuilder().id(userDto.getId()).build();
    }

    public static UserDto toItemDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}
