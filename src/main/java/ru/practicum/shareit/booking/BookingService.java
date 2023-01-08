package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingIncomeDto;
import ru.practicum.shareit.booking.model.BookingState;

import java.util.List;

public interface BookingService {
    BookingDto create(BookingIncomeDto bookingDto, Long userId);

    BookingDto getById(long id, long userId);

    BookingDto confirm(long id, long userId, boolean approved);

    List<BookingDto> getAllByBooker(BookingState state, long userId);

    List<BookingDto> getAllByOwner(BookingState state, long userId);
}
