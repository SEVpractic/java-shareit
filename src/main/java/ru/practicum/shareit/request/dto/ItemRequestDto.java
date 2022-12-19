package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Getter;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;

@Builder(toBuilder = true)
@Getter
public class ItemRequestDto {
    private final Long id;
    @NotBlank
    private final String description;
    @NotNull
    private final User requestor;
    @NotNull
    @PastOrPresent
    private final LocalDateTime created;
}
