package ru.practicum.shareit.booking.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.util.exceptions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;

    @Override
    public Booking create(Booking booking) {
        if (!booking.getItem().getIsAvailable()) {
            throw new CreationErrorException("Невозможно создать запрос вещи с признаком доступности false");
        }
        if (Objects.equals(booking.getItem().getOwner().getId(), booking.getBooker().getId())) {
            throw new UserNotValidException("Невозможно создать запрос вещи от владальца");
        }

        log.info("Создан запрос на бронирование с id = {} ", booking.getId());
        return bookingRepository.save(booking);
    }

    @Override
    public Booking confirm(long id, long userId, Boolean approved) {
        Booking booking = getById(id);

        if (booking.getStatus().equals(BookingStatus.APPROVED) ||
                booking.getStatus().equals(BookingStatus.REJECTED)) {
            throw new BookingPatchException("Статус бронирования уже был изменен");
        }
        if (booking.getItem().getOwner().getId() != userId) {
            throw new UserNotValidException("Изменять статус бронирования может только владелец вещи");
        }

        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
            log.info("Владелец подтвердил запрос на бронирование с id = {} ", booking.getId());
        } else {
            booking.setStatus(BookingStatus.REJECTED);
            log.info("Владелец отклонил запрос на бронирование с id = {} ", booking.getId());
        }

        return bookingRepository.save(booking);
    }

    @Override
    public Booking getById(long id, long userId) {
        Booking booking = getById(id);

        if (booking.getBooker().getId() != userId && booking.getItem().getOwner().getId() != userId) {
            throw new UserNotValidException("Данные о конкретном бронировании (включая его статус) может видеть " +
                    "автор бронирования, либо владельцем вещи, к которой относится бронирование.");
        }
        log.info("Возвращен запрос на бронирование с id = {} ", id);
        return booking;
    }

    @Override
    public List<Booking> getAllByOwner(BookingState state, long userId) {
        log.info("Возвращена коллекция запросов на бронирование владельца id = {} ", userId);

        switch (state) {
            case ALL: // все
                return bookingRepository.findAllByOwner(userId);
            case CURRENT: // текущие
                return bookingRepository.findAllCurrentByOwner(userId);
            case PAST: // завершённые
                return bookingRepository.findAllPastByOwner(userId);
            case FUTURE: // будущие
                return bookingRepository.findAllFutureByOwner(userId);
            case WAITING: // ожидающие подтверждения
                return bookingRepository.findAllWaitingByOwner(userId);
            case REJECTED: // отклонённые
                return bookingRepository.findAllRejectedByOwner(userId);
            default:
                throw new UnsupportedStatusException("Неподдерживаемый параметр BookingState");
        }
    }

    @Override
    public List<Booking> getAllByBooker(BookingState state, long userId) {
        log.info("Возвращена коллекция запросов на бронирование пользователя id = {} ", userId);

        switch (state) {
            case ALL: // все
                return bookingRepository.findAllByBooker(userId);
            case CURRENT: // текущие
                return bookingRepository.findAllCurrentByBooker(userId);
            case PAST: // завершённые
                return bookingRepository.findAllPastByBooker(userId);
            case FUTURE: // будущие
                return bookingRepository.findAllFutureByBooker(userId);
            case WAITING: // ожидающие подтверждения
                return bookingRepository.findAllWaitingByBooker(userId);
            case REJECTED: // отклонённые
                return bookingRepository.findAllRejectedByBooker(userId);
            default:
                throw new UnsupportedStatusException("Неподдерживаемый параметр BookingState");
        }
    }

    @Override
    public List<Booking> findNearest(long itemId) {
        List<Booking> bookings = new ArrayList<>();

        bookings.add(0, bookingRepository.findLastByItemId(itemId));
        bookings.add(1, bookingRepository.findNextByItemId(itemId));

        log.info("Возвращены последний и следующий запросы для вещи id = {} ", itemId);
        return bookings;
    }

    @Override
    public void checkItemBooking(Long bookerId, Long itemId) {
        if (bookingRepository.findToCheck(itemId, bookerId) < 1) {
            throw new CreationErrorException("Оставлять отзыв может только пользователь, использовавший вещь");
        }
    }

    private Booking getById(long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotExistException(
                        String.format("Зарпос на бронирование с id = %s не существует", id))
                );
    }
}
