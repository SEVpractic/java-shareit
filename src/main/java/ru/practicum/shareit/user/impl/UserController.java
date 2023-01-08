package ru.practicum.shareit.user.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserIncomeDto;
import ru.practicum.shareit.util.validation.CreateValidationGroup;
import ru.practicum.shareit.util.validation.UpdateValidationGroup;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Validated
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> getAll() {
        return userService.getAll();
    }

    @GetMapping("/{userId}")
    public UserDto getById(@PathVariable("userId") @Positive long userId) {
        return userService.getById(userId);
    }

    @PostMapping
    public UserDto create(@Validated(CreateValidationGroup.class) @RequestBody UserIncomeDto userDto) {
        return userService.create(userDto);
    }

    @PatchMapping("/{userId}")
    public UserDto update(@Validated(UpdateValidationGroup.class) @RequestBody UserIncomeDto userDto,
                          @PathVariable("userId") @Positive long userId) {
        return userService.update(userDto, userId);
    }

    @DeleteMapping("/{userId}")
    public void deleteById(@PathVariable("userId") @Positive long userId) {
        userService.deleteById(userId);
    }

    @DeleteMapping
    public void deleteAll() {
        userService.deleteAll();
    }
}
