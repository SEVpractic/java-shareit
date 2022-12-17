package ru.practicum.shareit.item.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.ItemStorage;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.util.exceptions.CreationErrorException;
import ru.practicum.shareit.util.exceptions.EntityNotExistException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class InMemoryItemStorage implements ItemStorage {
    private final Map<Long, Item> items = new HashMap<>();
    private long id = 0;

    @Override
    public List<Item> getAll() {
        return new ArrayList<>(items.values());
    }

    @Override
    public Item getById(long id) {
        Item item = items.get(id);
        if (item == null) {
            log.info("Вещь c id = {} не существует", id);
            throw new EntityNotExistException(
                    String.format("Вещь c id = %s не существует", id)
            );
        }
        return item;
    }

    @Override
    public Item create(Item item) {
        item = updateId(item);

        if (items.containsKey(item.getId())) {
            log.info("Не удалось создать вещь = {}", item.getId());
            throw new CreationErrorException(String.format("Не удалось создать вещь с id = %s", item.getId()));
        }

        items.putIfAbsent(item.getId(), item);
        return item;
    }

    @Override
    public Item update(Item item) {
        Item aldItem = getById(item.getId());

        if (item.getDescription() != null) {
            aldItem = aldItem.toBuilder().description(item.getDescription()).build();
        }
        if (item.getName() != null) {
            aldItem = aldItem.toBuilder().name(item.getName()).build();
        }
        if (item.getAvailable() != null) {
            aldItem = aldItem.toBuilder().available(item.getAvailable()).build();
        }

        items.put(item.getId(), aldItem);
        return aldItem;
    }

    @Override
    public void deleteById(long id) {
        if (items.remove(id) == null) {
            log.info("Вещь c id = {} не существует", id);
            throw new EntityNotExistException(
                    String.format("Вещь c id = %s не существует", id)
            );
        }
    }

    @Override
    public void deleteAll() {
        items.clear();
    }

    private Item updateId(Item item) {
        id++;
        return item.toBuilder()
                .id(id)
                .build();
    }
}
