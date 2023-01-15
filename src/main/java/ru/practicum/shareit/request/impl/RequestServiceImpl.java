package ru.practicum.shareit.request.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDtoForOwner;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.RequestService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestIncomeDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.util.exceptions.EntityNotExistException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequestServiceImpl implements RequestService {
    private final UserRepository userRepository;
    private final ItemRequestRepository itemRequestRepository;
    private final ItemRepository itemRepository;

    @Override
    public ItemRequestDto create(ItemRequestIncomeDto requestDto, long userId) {
        User requestor = findUserById(userId);
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(requestDto, requestor);

        itemRequest = itemRequestRepository.save(itemRequest);

        return ItemRequestMapper.toItemRequestDto(itemRequest);
    }

    @Override
    public ItemRequestDtoForOwner getById(long requestId, long userId) {
        findUserById(userId);
        ItemRequest itemRequest = findById(requestId);
        List<Item> itemsByRequest = findItemsByRequest(itemRequest);

        log.info("Возвращен запрос c id = {} ", userId);
        return ItemRequestMapper.toItemRequestDtoForOwner(itemRequest, itemsByRequest);
    }

    @Override
    public List<ItemRequestDtoForOwner> getForOwner(long userId) {
        findUserById(userId);
        Sort sort = Sort.by(Sort.Direction.DESC, "created");
        List<ItemRequest> itemRequests = itemRequestRepository.findAllByRequestor_Id(userId, sort);
        Map<ItemRequest, List<Item>> itemsByRequests = findItemsByRequests(itemRequests);

        log.info("Возвращена коллекция запросов на бронирование владельца id = {} ", userId);
        return ItemRequestMapper.toItemRequestDtoForOwner(itemRequests, itemsByRequests);
    }

    private ItemRequest findById(long requestId) {
        return itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotExistException(
                        String.format("Запрос c id = %s не существует", requestId))
                );
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotExistException(
                        String.format("Пользователь c id = %s не существует", userId))
                );
    }

    private Map<ItemRequest, List<Item>> findItemsByRequests(List<ItemRequest> requests) {
        return itemRepository.findAllByRequestIdIn(requests)
                .stream()
                .collect(Collectors.groupingBy(Item::getItemRequest, Collectors.toList()));
    }

    private List<Item> findItemsByRequest(ItemRequest request) {
        return itemRepository.findAllByItemRequest(request);
    }
}
