package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findAllByOwner_Id(Long userId);
    @Query("select it from Item as it where it.isAvailable = true and " +
            "(lower(it.name) like concat('%', lower(?1) , '%') or " +
            "lower(it.description) like concat('%', lower(?1), '%')) ")
    List<Item> findByText(String text);

    void deleteByIdAndOwnerId(long itemId, long userId);
}
