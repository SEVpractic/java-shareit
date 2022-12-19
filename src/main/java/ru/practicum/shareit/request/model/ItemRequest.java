package ru.practicum.shareit.request.model;

import lombok.Builder;
import lombok.Getter;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Builder(toBuilder = true)
@Getter
public class ItemRequest {
    private final Long id;
    private final String description;
    private final User requestor;
    private final LocalDateTime created;
}
