package ru.practicum.shareit.booking.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class BookingPair {
    private final Booking lastBooking;
    private final Booking nextBooking;
}
