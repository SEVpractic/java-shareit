package ru.practicum.shareit.item;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findAllByOwner_IdOrderById(Long userId, Pageable pageable);

    @Query("select it from Item as it where it.isAvailable = true and " +
            "(lower(it.name) like concat('%', lower(?1) , '%') or " +
            "lower(it.description) like concat('%', lower(?1), '%')) ")
    List<Item> findByText(String text, Pageable pageable);

    void deleteItemByIdAndOwner_Id(long itemId, long userId);

    @Query("select it from Item as it where it.itemRequest in ?1")
    List<Item> findAllByRequestIdIn(List<ItemRequest> requests);

    List<Item> findAllByItemRequest(ItemRequest itemRequest);
}
