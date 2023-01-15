package ru.practicum.shareit.request.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.RequestService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestIncomeDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.util.exceptions.EntityNotExistException;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequestServiceImpl implements RequestService {
    private final UserRepository userRepository;
    private final ItemRequestRepository itemRequestRepository;

    @Override
    public ItemRequestDto create(ItemRequestIncomeDto requestDto, long userId) {
        User requestor = findUserById(userId);
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(requestDto, requestor);

        itemRequest = itemRequestRepository.save(itemRequest);

        return ItemRequestMapper.toItemRequestDto(itemRequest);
    }

    @Override
    public ItemRequestDto getById(long requestId, long userId) {
        findUserById(userId);
        ItemRequest itemRequest = findById(requestId);

        log.info("Возвращен запрос c id = {} ", userId);
        return ItemRequestMapper.toItemRequestDto(itemRequest);
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
}
