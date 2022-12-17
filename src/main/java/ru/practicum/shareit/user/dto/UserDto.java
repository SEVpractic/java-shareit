package ru.practicum.shareit.user.dto;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Builder;
import lombok.Getter;
import ru.practicum.shareit.util.CreateValidationGroup;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Builder(toBuilder = true)
@Getter
public class UserDto {
    @JsonSetter("userId")
    private final Long id;
    @NotBlank (groups = CreateValidationGroup.class)
    private final String name;
    @NotNull (groups = CreateValidationGroup.class)
    @Email (groups = CreateValidationGroup.class)
    private final String email;
}
