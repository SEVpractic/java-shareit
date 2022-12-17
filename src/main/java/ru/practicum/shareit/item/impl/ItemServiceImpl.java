package ru.practicum.shareit.item.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.ItemStorage;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserStorage;
import ru.practicum.shareit.util.exceptions.UpdateErrorException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    @Override
    public List<Item> getAll() {
        log.info("Возвращен список всех вещей");
        return itemStorage.getAll();
    }

    @Override
    public List<Item> getAllByUserId(long userId) {
        log.info("Возвращен список всех вещей пользователе с id = {}", userId);
        return getAll().stream()
                .filter(i -> i.getOwner().getId() == userId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> getAllByText(String text) {
        if (text.isBlank()) return new ArrayList<>();
        return getAll().stream()
                .filter(i -> i.getAvailable() == true)
                .filter(i -> textCheck(text, i))
                .collect(Collectors.toList());
    }

    @Override
    public Item getById(long id) {
        Item item = itemStorage.getById(id);
        log.info("Возвращена вещь c id = {} ", id);
        return item;
    }

    @Override
    public Item create(Item item) {
        item = item.toBuilder().owner(userStorage.getById(item.getOwner().getId())).build();
        item = itemStorage.create(item);
        log.info("Создана вещь c id = {} ", item.getId());
        return item;
    }

    @Override
    public Item update(Item item) {
        ownerIdCheck(item);
        item = itemStorage.update(item);
        log.info("Обновлена вещь c id = {} ", item.getId());
        return item;
    }

    @Override
    public void deleteById(long id) {
        itemStorage.deleteById(id);
        log.info("Удалена вещь c id = {} ", id);
    }

    @Override
    public void deleteAll() {
        itemStorage.deleteAll();
        log.info("Удалены все вещи");
    }

    private void ownerIdCheck(Item item) {
        long ownersId = item.getOwner().getId();
        long aldOwnersId = itemStorage.getById(item.getId()).getOwner().getId();

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

    private boolean textCheck(String text, Item item) {
        String name = item.getName().trim().toLowerCase();
        String description = item.getDescription().trim().toLowerCase();
        text = text.trim().toLowerCase();

        return name.contains(text) || description.contains(text);
    }
}
