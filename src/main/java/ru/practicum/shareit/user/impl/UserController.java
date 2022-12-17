package ru.practicum.shareit.user.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.util.CreateValidationGroup;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> getAll() {
        List<User> users = userService.getAll();
        return users.stream()
                .map(UserMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{userId}")
    public UserDto getById(@PathVariable("userId") @Positive long id) {
        User user = userService.getById(id);
        return UserMapper.toItemDto(user);
    }

    @PostMapping
    public UserDto create(@Validated(CreateValidationGroup.class) @RequestBody UserDto userDto) {
        User user = userService.create(UserMapper.toItem(userDto));
        return UserMapper.toItemDto(user);
    }

    @PatchMapping("/{userId}")
    public UserDto update(@Valid @RequestBody UserDto userDto,
                          @PathVariable("userId") @Positive Long id) {
        userDto = userDto.toBuilder().id(id).build();
        User user = userService.update(UserMapper.toItem(userDto));
        return UserMapper.toItemDto(user);
    }

    @DeleteMapping("/{userId}")
    public void deleteById(@PathVariable("userId") @Positive long id) {
        userService.deleteById(id);
    }

    @DeleteMapping
    public void deleteAll() {
        userService.deleteAll();
    }
}
