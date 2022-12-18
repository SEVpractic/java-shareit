package ru.practicum.shareit.item.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.ItemStorage;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.util.exceptions.EntityNotExistException;
import ru.practicum.shareit.util.exceptions.UpdateErrorException;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;

    @Override
    public List<Item> getAll() {
        log.info("Возвращен список всех вещей");
        return itemStorage.getAll();
    }

    @Override
    public List<Item> getAllByUserId(long userId) {
        log.info("Возвращен список всех вещей пользователе с id = {}", userId);
        return itemStorage.getAllByUserId(userId);
    }

    @Override
    public List<Item> getAllByText(String text) {
        log.info("Возвращен список всех вещей со словом \"{}\" в названии или описании", text);
        return itemStorage.getAllByText(text);
    }

    @Override
    public Item getById(long id) {
        log.info("Возвращена вещь c id = {} ", id);
        return itemStorage.getById(id)
                .orElseThrow(() -> new EntityNotExistException(
                        String.format("Вещь c id = %s не существует", id))
                );
    }

    @Override
    public Item create(Item item) {
        return itemStorage.create(item);
    }

    @Override
    public Item update(Item item) {
        ownerIdCheck(item);
        item = itemStorage.update(item);
        log.info("Обновлена вещь c id = {} ", item.getId());
        return item;
    }

    @Override
    public void deleteById(long itemId, long userId) {
        itemStorage.deleteById(itemId, userId);
        log.info("Удалена вещь c id = {} ", itemId);
    }

    @Override
    public void deleteAll() {
        itemStorage.deleteAll();
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
