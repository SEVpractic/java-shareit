package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemStorage {
    List<Item> getAll();

    List<Item> getAllByUserId(long userId);

    List<Item> getAllByText(String text);

    Optional<Item> getById(long id);

    Item create(Item item);

    Item update(Item item);

    void deleteById(long itemId, long userId);

    void deleteAll();
}
