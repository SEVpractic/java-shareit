package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder(toBuilder = true)
@Getter
public class BookingIncomeDto {
    private Long itemId;
    private final LocalDateTime start;
    private final LocalDateTime end;
}
