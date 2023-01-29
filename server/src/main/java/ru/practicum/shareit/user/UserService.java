package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserIncomeDto;

import java.util.List;

public interface UserService {
    List<UserDto> getAll();

    UserDto getById(long id);

    UserDto create(UserIncomeDto user);

    UserDto update(UserIncomeDto userDto, long userId);

    void deleteById(long id);

    void deleteAll();
}
