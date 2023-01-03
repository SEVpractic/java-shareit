package ru.practicum.shareit.user.dto;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Builder;
import lombok.Getter;
import ru.practicum.shareit.util.validation.CreateValidationGroup;
import ru.practicum.shareit.util.validation.UpdateValidationGroup;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Builder(toBuilder = true)
@Getter
public class UserDto {
    @JsonSetter("userId")
    private Long id;
    @NotBlank (groups = CreateValidationGroup.class)
    private final String name;
    @NotBlank (groups = CreateValidationGroup.class)
    @Email (groups = {CreateValidationGroup.class, UpdateValidationGroup.class})
    private final String email;

    public void setId(Long id) {
        this.id = id;
    }
}
