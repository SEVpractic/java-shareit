package ru.practicum.shareit.booking;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Sql(value = {"/test-schema.sql", "/users-create-test.sql", "/item-create-test.sql", "/all-bookings-create-test.sql"})
class BookingRepoTest {
    @Autowired
    private BookingRepository bookingRepository;
    private final int from = 0;
    private final int size = 10;
    private final long bookerId = 2L;
    private final long ownerId = 1L;
    private final Pageable pageable = PageRequest.of(
            from,
            size,
            Sort.by(Sort.Direction.DESC, "start")
    );

    @Test
    void findAllByBookerTest() {
       List<Booking> bookings = bookingRepository.findAllByBooker(bookerId, pageable).toList();
        Assertions.assertThat(bookings).hasSize(3);
    }

    @Test
    void findAllWaitingByBookerTest() {
        List<Booking> bookings = bookingRepository.findAllWaitingByBooker(bookerId, pageable).toList();
        Assertions.assertThat(bookings).hasSize(1);
    }

    @Test
    void findAllRejectedByBookerTest() {
        List<Booking> bookings = bookingRepository.findAllRejectedByBooker(bookerId, pageable).toList();
        Assertions.assertThat(bookings).hasSize(1);
    }

    @Test
    void findAllPastByBookerTest() {
        List<Booking> bookings = bookingRepository.findAllPastByBooker(bookerId, pageable).toList();
        Assertions.assertThat(bookings).hasSize(1);
    }

    @Test
    void findAllFutureByBookerTest() {
        List<Booking> bookings = bookingRepository.findAllFutureByBooker(bookerId, pageable).toList();
        Assertions.assertThat(bookings).hasSize(2);
    }

    @Test
    void findAllCurrentByBookerTest() {
        List<Booking> bookings = bookingRepository.findAllCurrentByBooker(bookerId, pageable).toList();
        Assertions.assertThat(bookings).isEmpty();
    }

    @Test
    void findAllByOwnerTest() {
        List<Booking> bookings = bookingRepository.findAllByOwner(ownerId, pageable).toList();
        Assertions.assertThat(bookings).hasSize(3);
    }

    @Test
    void findAllWaitingByOwnerTest() {
        List<Booking> bookings = bookingRepository.findAllWaitingByOwner(ownerId, pageable).toList();
        Assertions.assertThat(bookings).hasSize(1);
    }

    @Test
    void findAllRejectedByOwnerTest() {
        List<Booking> bookings = bookingRepository.findAllRejectedByOwner(ownerId, pageable).toList();
        Assertions.assertThat(bookings).hasSize(1);
    }

    @Test
    void findAllPastByOwnerTest() {
        List<Booking> bookings = bookingRepository.findAllPastByOwner(ownerId, pageable).toList();
        Assertions.assertThat(bookings).hasSize(1);
    }

    @Test
    void findAllFutureByOwnerTest() {
        List<Booking> bookings = bookingRepository.findAllFutureByOwner(ownerId, pageable).toList();
        Assertions.assertThat(bookings).hasSize(2);
    }

    @Test
    void findAllCurrentByOwnerTest() {
        List<Booking> bookings = bookingRepository.findAllCurrentByOwner(ownerId, pageable).toList();
        Assertions.assertThat(bookings).isEmpty();
    }

    @Test
    void findToCheck() {
        long itemId = 2L;
        Long bookings = bookingRepository.findToCheck(itemId, bookerId);

        assertTrue(bookings > 0);
    }
}
