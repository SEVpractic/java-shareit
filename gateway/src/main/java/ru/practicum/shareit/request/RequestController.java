package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestIncomeDto;
import ru.practicum.shareit.util.validation.CreateValidationGroup;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Validated
public class RequestController {
    private final RequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> create(@Validated(CreateValidationGroup.class) @RequestBody ItemRequestIncomeDto requestDto,
                                         @RequestHeader("X-Sharer-User-Id") @Positive long userId) {
        return requestClient.create(requestDto, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getForOwner(@RequestHeader("X-Sharer-User-Id") @Positive long userId) {
        return requestClient.getForOwner(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAll(@RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                         @RequestParam(defaultValue = "10") @Positive int size,
                                         @RequestHeader("X-Sharer-User-Id") @Positive long userId) {
        return requestClient.getAll(from, size, userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getById(@PathVariable("requestId") @Positive long requestId,
                                          @RequestHeader("X-Sharer-User-Id") @Positive long userId) {
        return requestClient.getById(requestId, userId);
    }
}
