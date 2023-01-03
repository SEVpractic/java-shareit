package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    List<Item> getAll();

    List<Item> getAllByUserId(long userId);

    List<Item> getAllByText(String text);

    List<Comment> getByItemId(Long itemId);

    Item getById(long id);

    Item create(Item item);

    Item update(Item item);

    Comment addComment(Comment comment);

    void deleteById(long itemId, long userId);

    void deleteAll();
}
