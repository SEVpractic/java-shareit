package ru.practicum.shareit.user.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.util.validation.CreateValidationGroup;
import ru.practicum.shareit.util.validation.UpdateValidationGroup;

import javax.validation.constraints.Positive;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Validated
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> getAll() {
        List<User> users = userService.getAll();
        return users.stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{userId}")
    public UserDto getById(@PathVariable("userId") @Positive long id) {
        User user = userService.getById(id);
        return UserMapper.toUserDto(user);
    }

    @PostMapping
    public UserDto create(@Validated(CreateValidationGroup.class) @RequestBody UserDto userDto) {
        User user = userService.create(UserMapper.toUser(userDto));
        return UserMapper.toUserDto(user);
    }

    @PatchMapping("/{userId}")
    public UserDto update(@Validated(UpdateValidationGroup.class) @RequestBody UserDto userDto,
                          @PathVariable("userId") @Positive Long id) {
        userDto.setId(id);
        User user = userService.update(UserMapper.toUser(userDto));
        return UserMapper.toUserDto(user);
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
