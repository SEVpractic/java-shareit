package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.impl.BookingServiceImpl;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingMockServiceTest {
    @InjectMocks
    private BookingServiceImpl bookingService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BookingRepository bookingRepository;

    @Test
    void getAllByOwner_allState_callAllQuery() {
        long userId = 1L;
        int from = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(
                from,
                size,
                Sort.by(Sort.Direction.DESC, "start")
        );
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(new User()));
        when(bookingRepository.findAllByOwner(userId, pageable))
                .thenReturn(Page.empty());

        bookingService.getAllByOwner(from, size, BookingState.ALL, userId);

        verify(bookingRepository).findAllByOwner(userId, pageable);
    }

    @Test
    void getAllByOwner_allCurrent_callCurrentQuery() {
        long userId = 1L;
        int from = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(
                from,
                size,
                Sort.by(Sort.Direction.DESC, "start")
        );
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(new User()));
        when(bookingRepository.findAllCurrentByOwner(userId, pageable))
                .thenReturn(Page.empty());

        bookingService.getAllByOwner(from, size, BookingState.CURRENT, userId);

        verify(bookingRepository).findAllCurrentByOwner(userId, pageable);
    }

    @Test
    void getAllByOwner_allPast_callPastQuery() {
        long userId = 1L;
        int from = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(
                from,
                size,
                Sort.by(Sort.Direction.DESC, "start")
        );
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(new User()));
        when(bookingRepository.findAllPastByOwner(userId, pageable))
                .thenReturn(Page.empty());

        bookingService.getAllByOwner(from, size, BookingState.PAST, userId);

        verify(bookingRepository).findAllPastByOwner(userId, pageable);
    }

    @Test
    void getAllByOwner_allFuture_callFutureQuery() {
        long userId = 1L;
        int from = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(
                from,
                size,
                Sort.by(Sort.Direction.DESC, "start")
        );
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(new User()));
        when(bookingRepository.findAllFutureByOwner(userId, pageable))
                .thenReturn(Page.empty());

        bookingService.getAllByOwner(from, size, BookingState.FUTURE, userId);

        verify(bookingRepository).findAllFutureByOwner(userId, pageable);
    }

    @Test
    void getAllByOwner_allWaiting_calWaitingQuery() {
        long userId = 1L;
        int from = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(
                from,
                size,
                Sort.by(Sort.Direction.DESC, "start")
        );
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(new User()));
        when(bookingRepository.findAllWaitingByOwner(userId, pageable))
                .thenReturn(Page.empty());

        bookingService.getAllByOwner(from, size, BookingState.WAITING, userId);

        verify(bookingRepository).findAllWaitingByOwner(userId, pageable);
    }

    @Test
    void getAllByOwner_allRejected_calRejectedQuery() {
        long userId = 1L;
        int from = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(
                from,
                size,
                Sort.by(Sort.Direction.DESC, "start")
        );
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(new User()));
        when(bookingRepository.findAllRejectedByOwner(userId, pageable))
                .thenReturn(Page.empty());

        bookingService.getAllByOwner(from, size, BookingState.REJECTED, userId);

        verify(bookingRepository).findAllRejectedByOwner(userId, pageable);
    }

    @Test
    void getAllByBooker_allState_callAllQuery() {
        long userId = 1L;
        int from = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(
                from,
                size,
                Sort.by(Sort.Direction.DESC, "start")
        );
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(new User()));
        when(bookingRepository.findAllByBooker(userId, pageable))
                .thenReturn(Page.empty());

        bookingService.getAllByBooker(from, size, BookingState.ALL, userId);

        verify(bookingRepository).findAllByBooker(userId, pageable);
    }

    @Test
    void getAllByBooker_allCurrent_callCurrentQuery() {
        long userId = 1L;
        int from = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(
                from,
                size,
                Sort.by(Sort.Direction.DESC, "start")
        );
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(new User()));
        when(bookingRepository.findAllCurrentByBooker(userId, pageable))
                .thenReturn(Page.empty());

        bookingService.getAllByBooker(from, size, BookingState.CURRENT, userId);

        verify(bookingRepository).findAllCurrentByBooker(userId, pageable);
    }

    @Test
    void getAllByBooker_allPast_callPastQuery() {
        long userId = 1L;
        int from = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(
                from,
                size,
                Sort.by(Sort.Direction.DESC, "start")
        );
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(new User()));
        when(bookingRepository.findAllPastByBooker(userId, pageable))
                .thenReturn(Page.empty());

        bookingService.getAllByBooker(from, size, BookingState.PAST, userId);

        verify(bookingRepository).findAllPastByBooker(userId, pageable);
    }

    @Test
    void getAllByBooker_allFuture_callFutureQuery() {
        long userId = 1L;
        int from = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(
                from,
                size,
                Sort.by(Sort.Direction.DESC, "start")
        );
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(new User()));
        when(bookingRepository.findAllFutureByBooker(userId, pageable))
                .thenReturn(Page.empty());

        bookingService.getAllByBooker(from, size, BookingState.FUTURE, userId);

        verify(bookingRepository).findAllFutureByBooker(userId, pageable);
    }

    @Test
    void getAllByBooker_allWaiting_calWaitingQuery() {
        long userId = 1L;
        int from = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(
                from,
                size,
                Sort.by(Sort.Direction.DESC, "start")
        );
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(new User()));
        when(bookingRepository.findAllWaitingByBooker(userId, pageable))
                .thenReturn(Page.empty());

        bookingService.getAllByBooker(from, size, BookingState.WAITING, userId);

        verify(bookingRepository).findAllWaitingByBooker(userId, pageable);
    }

    @Test
    void getAllByBooker_allRejected_calRejectedQuery() {
        long userId = 1L;
        int from = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(
                from,
                size,
                Sort.by(Sort.Direction.DESC, "start")
        );
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(new User()));
        when(bookingRepository.findAllRejectedByBooker(userId, pageable))
                .thenReturn(Page.empty());

        bookingService.getAllByBooker(from, size, BookingState.REJECTED, userId);

        verify(bookingRepository).findAllRejectedByBooker(userId, pageable);
    }
}
