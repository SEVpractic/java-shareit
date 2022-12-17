package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.util.Crud;

import java.util.List;

public interface ItemService extends Crud<Item> {
    List<Item> getAllByUserId(long userId);

    List<Item> getAllByText(String text);
}
