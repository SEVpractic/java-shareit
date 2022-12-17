package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Getter;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

@Builder(toBuilder = true)
@Getter
public class Item {
    private final Long id;
    private final String name;
    private final String description;
    private final Boolean available;
    private final User owner;
    private final ItemRequest request;
}
