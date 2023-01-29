package ru.practicum.shareit.user;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserIncomeDto;
import ru.practicum.shareit.util.validation.CreateValidationGroup;
import ru.practicum.shareit.util.validation.UpdateValidationGroup;

import javax.validation.constraints.Positive;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Validated
public class UserController {
    private final UserClient userClient;

    @GetMapping
    public ResponseEntity<Object> getAll() {
        return userClient.getAll();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getById(@PathVariable("userId") @Positive long userId) {
        return userClient.getById(userId);
    }

    @PostMapping
    public ResponseEntity<Object> create(@Validated(CreateValidationGroup.class) @RequestBody UserIncomeDto userDto) {
        return userClient.create(userDto);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> update(@Validated(UpdateValidationGroup.class) @RequestBody UserIncomeDto userDto,
                                         @PathVariable("userId") @Positive long userId) {
        return userClient.update(userDto, userId);
    }

    @DeleteMapping("/{userId}")
    public void deleteById(@PathVariable("userId") @Positive long userId) {
        userClient.deleteById(userId);
    }

    @DeleteMapping
    public void deleteAll() {
        userClient.deleteAll();
    }
}
