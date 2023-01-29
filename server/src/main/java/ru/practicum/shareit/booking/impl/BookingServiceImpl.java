package ru.practicum.shareit.booking.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingIncomeDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.util.exceptions.*;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    @Transactional
    public BookingDto create(BookingIncomeDto bookingDto, Long userId) {
        Booking booking = fillBooking(bookingDto, userId);
        bookingCreationCheck(booking);

        booking = bookingRepository.save(booking);
        log.info("Создан запрос на бронирование с id = {} ", booking.getId());
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    @Transactional
    public BookingDto confirm(long id, long userId, boolean approved) {
        Booking booking = findById(id);
        bookingConfirmCheck(booking, userId);

        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
            log.info("Владелец подтвердил запрос на бронирование с id = {} ", booking.getId());
        } else {
            booking.setStatus(BookingStatus.REJECTED);
            log.info("Владелец отклонил запрос на бронирование с id = {} ", booking.getId());
        }

        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public BookingDto getById(long id, long userId) {
        Booking booking = findById(id);

        if (booking.getBooker().getId() != userId && booking.getItem().getOwner().getId() != userId) {
            throw new UserNotValidException("Данные о конкретном бронировании (включая его статус) может видеть " +
                    "автор бронирования, либо владельцем вещи, к которой относится бронирование.");
        }

        log.info("Возвращен запрос на бронирование с id = {} ", id);
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public List<BookingDto> getAllByOwner(int from, int size, BookingState state, long userId) {
        List<Booking> bookings;
        findUserById(userId);
        Pageable pageable = PageRequest.of(
                from == 0 ? 0 : (from / size),
                size,
                Sort.by(Sort.Direction.DESC, "start")
        );

        switch (state) {
            case CURRENT: // текущие
                bookings = bookingRepository.findAllCurrentByOwner(userId, pageable).toList();
                break;
            case PAST: // завершённые
                bookings = bookingRepository.findAllPastByOwner(userId, pageable).toList();
                break;
            case FUTURE: // будущие
                bookings = bookingRepository.findAllFutureByOwner(userId, pageable).toList();
                break;
            case WAITING: // ожидающие подтверждения
                bookings = bookingRepository.findAllWaitingByOwner(userId, pageable).toList();
                break;
            case REJECTED: // отклонённые
                bookings = bookingRepository.findAllRejectedByOwner(userId, pageable).toList();
                break;
            default: // все
                bookings = bookingRepository.findAllByOwner(userId, pageable).toList();
                break;
        }

        log.info("Возвращена коллекция запросов на бронирование владельца id = {} ", userId);
        return BookingMapper.toBookingDto(bookings);
    }

    @Override
    public List<BookingDto> getAllByBooker(int from, int size, BookingState state, long userId) {
        List<Booking> bookings;
        findUserById(userId);
        Pageable pageable = PageRequest.of(
                from == 0 ? 0 : (from / size),
                size,
                Sort.by(Sort.Direction.DESC, "start")
        );

        switch (state) {
            case CURRENT: // текущие
                bookings = bookingRepository.findAllCurrentByBooker(userId, pageable).toList();
                break;
            case PAST: // завершённые
                bookings = bookingRepository.findAllPastByBooker(userId, pageable).toList();
                break;
            case FUTURE: // будущие
                bookings = bookingRepository.findAllFutureByBooker(userId, pageable).toList();
                break;
            case WAITING: // ожидающие подтверждения
                bookings = bookingRepository.findAllWaitingByBooker(userId, pageable).toList();
                break;
            case REJECTED: // отклонённые
                bookings = bookingRepository.findAllRejectedByBooker(userId, pageable).toList();
                break;
            default: // все
                bookings = bookingRepository.findAllByBooker(userId, pageable).toList();
                break;
        }

        log.info("Возвращена коллекция запросов на бронирование пользователя id = {} ", userId);
        return BookingMapper.toBookingDto(bookings);
    }

    private Booking findById(long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotExistException(
                        String.format("Зарпос на бронирование с id = %s не существует", id))
                );
    }

    private void bookingCreationCheck(Booking booking) {
        if (!booking.getItem().isAvailable()) {
            throw new CreationErrorException("Невозможно создать запрос вещи с признаком доступности false");
        }
        if (Objects.equals(booking.getItem().getOwner().getId(), booking.getBooker().getId())) {
            throw new UserNotValidException("Невозможно создать запрос вещи от владальца");
        }
    }

    private void bookingConfirmCheck(Booking booking, Long userId) {
        if (booking.getStatus().equals(BookingStatus.APPROVED) ||
                booking.getStatus().equals(BookingStatus.REJECTED)) {
            throw new BookingPatchException("Статус бронирования уже был изменен");
        }
        if (!Objects.equals(booking.getItem().getOwner().getId(), userId)) {
            throw new UserNotValidException("Изменять статус бронирования может только владелец вещи");
        }
    }

    private Booking fillBooking(BookingIncomeDto bookingDto, Long userId) {
        Booking booking = BookingMapper.toBooking(bookingDto);

        booking.setStatus(BookingStatus.WAITING);
        booking.setBooker(findUserById(userId));
        booking.setItem(findItemById(bookingDto.getItemId()));

        return booking;
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(
                        () -> new EntityNotExistException(
                                String.format("Пользователь c id = %s не существует", userId))
                );
    }

    private Item findItemById(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(
                        () -> new EntityNotExistException(
                                String.format("Вещь c id = %s не существует", itemId))
                );
    }
}
