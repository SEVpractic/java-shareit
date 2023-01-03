package ru.practicum.shareit.item.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.CommentRepository;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.util.exceptions.EntityNotExistException;
import ru.practicum.shareit.util.exceptions.UpdateErrorException;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;

    @Override
    public List<Item> getAll() {
        log.info("Возвращен список всех вещей");
        return itemRepository.findAll();
    }

    @Override
    public List<Item> getAllByUserId(long userId) {
        log.info("Возвращен список всех вещей пользователе с id = {}", userId);
        return itemRepository.findAllByOwner_IdOrderById(userId);
    }

    @Override
    public List<Item> getAllByText(String text) {
        text = text.trim().toLowerCase();
        log.info("Возвращен список всех вещей со словом \"{}\" в названии или описании", text);
        return itemRepository.findByText(text);
    }

    @Override
    public Item getById(long id) {
        log.info("Возвращена вещь c id = {} ", id);
        return itemRepository.findById(id)
                .orElseThrow(() -> new EntityNotExistException(
                        String.format("Вещь c id = %s не существует", id))
                );
    }

    @Override
    public List<Comment> getByItemId(Long itemId) {
        log.info("Возвращен сипок коментарием для вещи c id = {} ", itemId);
        return commentRepository.findAllByItem_Id(itemId);
    }

    @Override
    public Item create(Item item) {
        log.info("Создана вещь c id = {} ", item.getId());
        return itemRepository.save(item);
    }

    @Override
    public Item update(Item item) {
        ownerIdCheck(item);

        Long itemId = item.getId();
        Item oldItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotExistException(
                        String.format("Вещь c id = %s не существует", itemId))
                );

        if (item.getDescription() != null && !item.getDescription().isBlank()) {
            oldItem.setDescription(item.getDescription());
        }
        if (item.getName() != null && !item.getName().isBlank()) {
            oldItem.setName(item.getName());
        }
        if (item.getIsAvailable() != null) {
            oldItem.setIsAvailable(item.getIsAvailable());
        }

        oldItem = itemRepository.save(oldItem);
        log.info("Обновлена вещь c id = {} ", item.getId());
        return oldItem;
    }

    @Override
    public void deleteById(long itemId, long userId) {
        itemRepository.deleteByIdAndOwnerId(itemId, userId);
        log.info("Удалена вещь c id = {} ", itemId);
    }

    @Override
    public Comment addComment(Comment comment) {
        log.info("Добавлен комментиарий вещи c id = {} от пользователя с id = {}",
                comment.getItem().getId(), comment.getAuthor().getId());
        return commentRepository.save(comment);
    }

    @Override
    public void deleteAll() {
        itemRepository.deleteAll();
        log.info("Удалены все вещи");
    }

    private void ownerIdCheck(Item item) {
        long ownersId = item.getOwner().getId();
        long aldOwnersId = getById(item.getId()).getOwner().getId();

        if (ownersId != aldOwnersId) {
            log.info("Ошибка обновления вещи c id = {} ", item.getId());
            throw new UpdateErrorException(
                    String.format(
                            "Ошибка обновления вещи c id = %s , невозможно обновить вещь другого пользователя",
                            item.getId()
                    )
            );
        }
    }
}
