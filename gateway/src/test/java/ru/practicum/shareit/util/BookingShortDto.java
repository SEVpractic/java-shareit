package ru.practicum.shareit.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class BookingShortDto {
    private final Long id;
    private final Long bookerId;
}
