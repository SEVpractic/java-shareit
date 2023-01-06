package ru.practicum.shareit.booking;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("select b from Booking as b where b.booker.id = ?1")
    List<Booking> findAllByBooker(long userId, Sort sort);

    @Query("select b from Booking as b where b.booker.id = ?1 and b.status = 'WAITING'")
    List<Booking> findAllWaitingByBooker(long userId, Sort sort);

    @Query("select b from Booking as b where b.booker.id = ?1 and b.status = 'REJECTED'")
    List<Booking> findAllRejectedByBooker(long userId, Sort sort);

    @Query("select b from Booking as b where b.booker.id = ?1 and b.end < current_timestamp")
    List<Booking> findAllPastByBooker(long userId, Sort sort);

    @Query("select b from Booking as b where b.booker.id = ?1 and b.start > current_timestamp")
    List<Booking> findAllFutureByBooker(long userId, Sort sort);

    @Query("select b from Booking as b where b.booker.id = ?1 and " +
            "current_timestamp between b.start and b.end")
    List<Booking> findAllCurrentByBooker(long userId, Sort sort);

    @Query("select b from Booking as b where b.item.owner.id = ?1")
    List<Booking> findAllByOwner(long userId, Sort sort);

    @Query("select b from Booking as b where b.item.owner.id = ?1 and b.status = 'WAITING'")
    List<Booking> findAllWaitingByOwner(long userId, Sort sort);

    @Query("select b from Booking as b where b.item.owner.id = ?1 and b.status = 'REJECTED'")
    List<Booking> findAllRejectedByOwner(long userId, Sort sort);

    @Query("select b from Booking as b where b.item.owner.id = ?1 and b.end < current_timestamp")
    List<Booking> findAllPastByOwner(long userId, Sort sort);

    @Query("select b from Booking as b where b.item.owner.id = ?1 and b.start > current_timestamp")
    List<Booking> findAllFutureByOwner(long userId, Sort sort);

    @Query("select b from Booking as b where b.item.owner.id = ?1 and " +
            "current_timestamp between b.start and b.end")
    List<Booking> findAllCurrentByOwner(long userId, Sort sort);

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

    @Query("select b from Booking as b where b.item in ?1 and " +
            "b.start = (select min(bo.start) from Booking as bo where bo.start > current_timestamp) or " +
            "b.end = (select max(bo.end) from Booking as bo where bo.end < current_timestamp) " +
            "order by b.start ")
    List<Booking> findNearlyBookingByItemIn(List<Item> items);

    @Query("select count(b) from Booking  as b where b.item.id = ?1 and " +
            "b.booker.id = ?2 and b.end < current_timestamp ")
    Long findToCheck(Long itemId, Long bookerId);
}
