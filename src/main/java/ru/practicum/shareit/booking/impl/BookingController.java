package ru.practicum.shareit.booking.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.UserService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Validated
public class BookingController {
    private final BookingService bookingService;
    private final UserService userService;
    private final ItemService itemService;

    @PostMapping
    public BookingDto create(@Validated @RequestBody BookingDto bookingDto,
                             @RequestHeader("X-Sharer-User-Id") @Positive long userId) {
        Booking booking = BookingMapper.toBooking(bookingDto);
        booking.setStatus(BookingStatus.WAITING);

        booking.setBooker(userService.getById(userId));
        booking.setItem(itemService.getById(bookingDto.getItemId()));

        booking = bookingService.create(booking);
        return BookingMapper.toBookingDto(booking);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto confirm(@PathVariable("bookingId") @Positive long id,
                              @RequestParam(name = "approved") @NotNull Boolean approved,
                              @RequestHeader("X-Sharer-User-Id") @Positive long userId) {
        Booking booking = bookingService.confirm(id, userId, approved);
        return BookingMapper.toBookingDto(booking);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getById(@PathVariable("bookingId") @Positive long id,
                              @RequestHeader("X-Sharer-User-Id") @Positive long userId) {
        Booking booking = bookingService.getById(id, userId);
        return BookingMapper.toBookingDto(booking);
    }

    @GetMapping
    public List<BookingDto> getAllByBooker(@Valid @RequestParam(name = "state", defaultValue = "ALL") BookingState state,
                                           @RequestHeader("X-Sharer-User-Id") @Positive long userId) {
        userService.getById(userId);
        List<Booking> bookings = bookingService.getAllByBooker(state, userId);

        return bookings.stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllByOwner(@Valid @RequestParam(name = "state", defaultValue = "ALL") BookingState state,
                                          @RequestHeader("X-Sharer-User-Id") @Positive long userId) {
        userService.getById(userId);
        List<Booking> bookings = bookingService.getAllByOwner(state, userId);

        return bookings.stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

}
