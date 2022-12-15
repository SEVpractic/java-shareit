package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Getter;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Builder(toBuilder = true)
@Getter
public class ItemDto {
    private final Long id;
    @NotBlank
    @Size(max = 50)
    private final String name;
    @NotBlank
    private final String description;
    @NotNull
    private final Boolean available;
    @NotNull
    private final User owner;
    @NotNull
    private final ItemRequest request;
}
