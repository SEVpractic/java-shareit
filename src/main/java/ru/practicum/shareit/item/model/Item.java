package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.user.model.User;

@Builder(toBuilder = true)
@Setter
@Getter
public class Item {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private User owner;
}
