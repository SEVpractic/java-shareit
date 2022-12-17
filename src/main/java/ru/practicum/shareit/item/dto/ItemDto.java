package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Getter;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.util.CreateValidationGroup;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Builder(toBuilder = true)
@Getter
public class ItemDto {
    private final Long id;
    @NotBlank (groups = CreateValidationGroup.class)
    @Size(max = 50)
    private final String name;
    @NotBlank (groups = CreateValidationGroup.class)
    private final String description;
    @NotNull (groups = CreateValidationGroup.class)
    private final Boolean available;
    private final User owner;
    private final ItemRequest request;
}
