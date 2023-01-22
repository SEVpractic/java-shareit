package ru.practicum.shareit.booking;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("select distinct b from Booking as b " +
            "where b.booker.id = ?1 ")
    Page<Booking> findAllByBooker(long userId, Pageable pageable);

    @Query("select b from Booking as b where b.booker.id = ?1 and b.status = 'WAITING'")
    Page<Booking> findAllWaitingByBooker(long userId, Pageable pageable);

    @Query("select b from Booking as b where b.booker.id = ?1 and b.status = 'REJECTED'")
    Page<Booking> findAllRejectedByBooker(long userId, Pageable pageable);

    @Query("select b from Booking as b where b.booker.id = ?1 and b.end < current_timestamp")
    Page<Booking> findAllPastByBooker(long userId, Pageable pageable);

    @Query("select b from Booking as b where b.booker.id = ?1 and b.start > current_timestamp")
    Page<Booking> findAllFutureByBooker(long userId, Pageable pageable);

    @Query("select b from Booking as b where b.booker.id = ?1 and " +
            "current_timestamp between b.start and b.end")
    Page<Booking> findAllCurrentByBooker(long userId, Pageable pageable);

    @Query("select b from Booking as b where b.item.owner.id = ?1")
    Page<Booking> findAllByOwner(long userId, Pageable pageable);

    @Query("select b from Booking as b where b.item.owner.id = ?1 and b.status = 'WAITING'")
    Page<Booking> findAllWaitingByOwner(long userId, Pageable pageable);

    @Query("select b from Booking as b where b.item.owner.id = ?1 and b.status = 'REJECTED'")
    Page<Booking> findAllRejectedByOwner(long userId, Pageable pageable);

    @Query("select b from Booking as b where b.item.owner.id = ?1 and b.end < current_timestamp")
    Page<Booking> findAllPastByOwner(long userId, Pageable pageable);

    @Query("select b from Booking as b where b.item.owner.id = ?1 and b.start > current_timestamp")
    Page<Booking> findAllFutureByOwner(long userId, Pageable pageable);

    @Query("select b from Booking as b where b.item.owner.id = ?1 and " +
            "current_timestamp between b.start and b.end")
    Page<Booking> findAllCurrentByOwner(long userId, Pageable pageable);

    @Query("select b from Booking as b where b.item in ?1 and b.status = 'APPROVED' and " +
            "(b.start = (select min(bo.start) from Booking as bo where bo.start > current_timestamp) or " +
            "b.end = (select max(bo.end) from Booking as bo where bo.end < current_timestamp or " +
            "bo.end = current_timestamp or bo.start = current_timestamp )) " +
            "order by b.start ")
    List<Booking> findNearlyBookingByItemIn(List<Item> items);

    @Query("select count(b) from Booking  as b where b.item.id = ?1 and " +
            "b.booker.id = ?2 and b.end < current_timestamp ")
    Long findToCheck(Long itemId, Long bookerId);
}
