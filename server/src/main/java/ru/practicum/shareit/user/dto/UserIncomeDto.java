package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Getter;

@Builder(toBuilder = true)
@Getter
public class UserIncomeDto {
    private final String name;
    private final String email;
}
