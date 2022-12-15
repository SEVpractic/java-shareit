package ru.practicum.shareit.user.model;

import lombok.Builder;
import lombok.Getter;

@Builder(toBuilder = true)
@Getter
public class User {
    private final Long id;
    private final String name;
    private final String email;
}
