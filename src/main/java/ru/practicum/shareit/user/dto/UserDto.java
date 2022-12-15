package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Builder(toBuilder = true)
@Getter
public class UserDto {
    private final Long id;
    @NotBlank
    private final String name;
    @NotNull
    @Email
    private final String email;
}
