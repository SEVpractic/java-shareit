package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingIncomeDto;
import ru.practicum.shareit.booking.dto.BookingState;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Validated
public class BookingController {
	private final BookingClient bookingClient;

	@PostMapping
	public ResponseEntity<Object> create(@Valid @RequestBody BookingIncomeDto bookingIncomeDto,
										 @RequestHeader("X-Sharer-User-Id") @Positive long userId) {
		return bookingClient.create(bookingIncomeDto, userId);
	}

	@PatchMapping("/{bookingId}")
	public ResponseEntity<Object> confirm(@PathVariable("bookingId") @Positive long id,
										  @RequestParam(name = "approved") boolean approved,
										  @RequestHeader("X-Sharer-User-Id") @Positive long userId) {
		return bookingClient.confirm(id, userId, approved);
	}

	@GetMapping("/{bookingId}")
	public ResponseEntity<Object> getById(@PathVariable("bookingId") @Positive long id,
										  @RequestHeader("X-Sharer-User-Id") @Positive long userId) {
		return bookingClient.getById(id, userId);
	}

	@GetMapping
	public ResponseEntity<Object> getAllByBooker(@RequestParam(name = "state", defaultValue = "ALL") BookingState state,
												 @RequestParam(defaultValue = "0") @PositiveOrZero int from,
												 @RequestParam(defaultValue = "10") @Positive int size,
												 @RequestHeader("X-Sharer-User-Id") @Positive long userId) {
		return bookingClient.getAllByBooker(from, size, state, userId);
	}

	@GetMapping("/owner")
	public ResponseEntity<Object> getAllByOwner(@RequestParam(name = "state", defaultValue = "ALL") BookingState state,
												@RequestParam(defaultValue = "0") @PositiveOrZero int from,
												@RequestParam(defaultValue = "10") @Positive int size,
												@RequestHeader("X-Sharer-User-Id") @Positive long userId) {
		return bookingClient.getAllByOwner(from, size, state, userId);
	}
}