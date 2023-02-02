package ru.practicum.shareit.util;

import lombok.Builder;
import lombok.Getter;
import ru.practicum.shareit.item.dto.CommentDto;

import java.util.List;

@Builder(toBuilder = true)
@Getter
public class ItemDto {
    private Long id;
    private final String name;
    private final String description;
    private final Boolean available;
    private final UserDto owner;
    private final BookingShortDto lastBooking;
    private final BookingShortDto nextBooking;
    private final List<CommentDto> comments;
    private final Long requestId;
}
