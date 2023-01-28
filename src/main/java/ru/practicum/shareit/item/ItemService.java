package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemIncomeDto;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;

public interface ItemService {

    ItemDto getById(long itemId, long userId);

    List<ItemDto> getAllByUserId(int from, int size, long userId);

    List<ItemDto> getAllByText(int from, int size, String text);

    List<Comment> getByItemId(Long itemId);

    ItemDto create(ItemIncomeDto itemDto, long userId);

    ItemDto update(ItemIncomeDto itemIncomeDto, Long itemId, Long userId);

    CommentDto addComment(CommentDto commentDto, long itemId, long userId);

    void deleteById(long itemId, long userId);

    void deleteAll();
}
