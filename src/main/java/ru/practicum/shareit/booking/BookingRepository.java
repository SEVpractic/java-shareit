package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("select b from Booking as b where b.booker.id = ?1 order by b.start desc")
    List<Booking> findAllByBooker(long userId);

    @Query("select b from Booking as b where b.booker.id = ?1 and b.status = 'WAITING'" +
            "order by b.start desc")
    List<Booking> findAllWaitingByBooker(long userId);

    @Query("select b from Booking as b where b.booker.id = ?1 and b.status = 'REJECTED'" +
            "order by b.start desc")
    List<Booking> findAllRejectedByBooker(long userId);

    @Query("select b from Booking as b where b.booker.id = ?1 and b.end < current_timestamp " +
            "order by b.start desc")
    List<Booking> findAllPastByBooker(long userId);

    @Query("select b from Booking as b where b.booker.id = ?1 and b.start > current_timestamp " +
            "order by b.start desc")
    List<Booking> findAllFutureByBooker(long userId);

    @Query("select b from Booking as b where b.booker.id = ?1 and b.start < current_timestamp " +
            "and b.end > current_timestamp order by b.start desc")
    List<Booking> findAllCurrentByBooker(long userId);

    @Query("select b from Booking as b where b.item.owner.id = ?1 order by b.start desc")
    List<Booking> findAllByOwner(long userId);

    @Query("select b from Booking as b where b.item.owner.id = ?1 and b.status = 'WAITING'" +
            "order by b.start desc")
    List<Booking> findAllWaitingByOwner(long userId);

    @Query("select b from Booking as b where b.item.owner.id = ?1 and b.status = 'REJECTED'" +
            "order by b.start desc")
    List<Booking> findAllRejectedByOwner(long userId);

    @Query("select b from Booking as b where b.item.owner.id = ?1 and b.end < current_timestamp " +
            "order by b.start desc")
    List<Booking> findAllPastByOwner(long userId);

    @Query("select b from Booking as b where b.item.owner.id = ?1 and b.start > current_timestamp " +
            "order by b.start desc")
    List<Booking> findAllFutureByOwner(long userId);

    @Query("select b from Booking as b where b.item.owner.id = ?1 and b.start < current_timestamp " +
            "and b.end > current_timestamp order by b.start desc")
    List<Booking> findAllCurrentByOwner(long userId);

    @Query("select b from Booking as b where b.item.id = ?1 and " +
            "b.end = (select max(bo.end) from Booking as bo where bo.end < current_timestamp) ")
    Booking findLastByItemId(long itemId);

    @Query("select b from Booking as b where b.item.id = ?1 and " +
            "b.start = (select min(bo.start) from Booking as bo where bo.start > current_timestamp) ")
    Booking findNextByItemId(long itemId);

    @Query("select b from Booking as b where b.item.id = ?1 and " +
            "b.start = (select min(bo.start) from Booking as bo where bo.start > current_timestamp) or " +
            "b.end = (select max(bo.end) from Booking as bo where bo.end < current_timestamp) " +
            "order by b.start ")
    List<Booking> findNearlyBookingByItemId(long itemId);
}
