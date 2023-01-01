package ru.practicum.shareit.item.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.ItemStorage;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.util.exceptions.EntityNotExistException;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class InMemoryItemStorage implements ItemStorage {
    private final Map<Long, List<Item>> userItemIndex = new LinkedHashMap<>(); // ключ - id пользователя
    private long id = 0;

    @Override
    public List<Item> getAll() {
        List<Item> items = new ArrayList<>();
        userItemIndex.values().forEach(items::addAll);
        return items;
    }

    @Override
    public List<Item> getAllByUserId(long userId) {
        return userItemIndex.get(userId);
    }

    @Override
    public List<Item> getAllByText(String text) {
        List<Item> items = new ArrayList<>();
        userItemIndex.values().forEach(items::addAll);
        return items.stream()
                .filter(i -> i.getIsAvailable() && textCheck(text, i))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Item> getById(long id) {
        List<Item> items = new ArrayList<>();
        userItemIndex.values().forEach(items::addAll);
        return items.stream()
                .filter(i -> i.getId() == id)
                .findFirst();
    }

    @Override
    public Item create(Item item) {
        updateId(item);
        userItemIndex.computeIfAbsent(item.getOwner().getId(), k -> new ArrayList<>()).add(item);
        log.info("Создана вещь c id = {} ", item.getId());
        return item;
    }

    @Override
    public Item update(Item item) {
        Item oldItem = userItemIndex.get(item.getOwner().getId()).stream()
                .filter(i -> i.getId() == (long) item.getId())
                .findFirst()
                .orElseThrow(() -> new EntityNotExistException(
                        String.format("Вещь c id = %s не существует", id))
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

        return oldItem;
    }

    @Override
    public void deleteById(long itemId, long userId) {
        userItemIndex.get(userId).remove(itemId);
    }

    @Override
    public void deleteAll() {
        userItemIndex.clear();
    }

    private void updateId(Item item) {
        id++;
        item.setId(id);
    }

    private boolean textCheck(String text, Item item) {
        String name = item.getName().trim().toLowerCase();
        String description = item.getDescription().trim().toLowerCase();
        text = text.trim().toLowerCase();

        return name.contains(text) || description.contains(text);
    }
}
