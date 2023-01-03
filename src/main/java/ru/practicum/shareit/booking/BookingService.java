package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.model.BookingPair;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;

import java.util.List;

public interface BookingService {
    Booking create(Booking booking);

    Booking getById(long id, long userId);

    List<Booking> getAllByBooker(BookingState state, long userId);

    Booking confirm(long id, long userId, Boolean approved);

    List<Booking> getAllByOwner(BookingState state, long userId);

    BookingPair findNearest(long itemId);
}
