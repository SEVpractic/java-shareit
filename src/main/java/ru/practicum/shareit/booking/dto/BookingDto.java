package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Getter;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Builder(toBuilder = true)
@Getter
public class BookingDto {
    private final Long id;
    @NotNull
    @FutureOrPresent
    private final LocalDateTime start;
    @NotNull
    @FutureOrPresent
    private final LocalDateTime end;
    @NotNull
    private final Item item;
    @NotNull
    private final User booker;
    @NotNull
    private final BookingStatus status;
}