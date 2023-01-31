package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.dto.BookingIncomeDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.util.exceptions.UnsupportedStatusException;

import java.util.Map;

import static ru.practicum.shareit.booking.dto.BookingState.UNSUPPORTED_STATUS;

@Service
@Slf4j
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> create(BookingIncomeDto bookingIncomeDto, long userId) {
        log.info("Обработка запроса создания аренды от пользователя c id = {} ", userId);
        return post("", userId, bookingIncomeDto);
    }

    public ResponseEntity<Object> confirm(long id, long userId, boolean approved) {
        Map<String, Object> parameters = Map.of("approved", approved);

        log.info("Обработка запроса подтверждения аренды c id = {} ", id);
        return patch("/" + id + "?approved={approved}", userId, parameters, null);
    }

    public ResponseEntity<Object> getById(long id, long userId) {
        log.info("Обработка запроса на возврат аренды c id = {} ", id);
        return get("/" + id, userId);
    }

    public ResponseEntity<Object> getAllByBooker(int from, int size, BookingState state, long userId) {
        if (state.equals(UNSUPPORTED_STATUS)) {
            throw new UnsupportedStatusException("Неподдерживаемый параметр BookingState");
        }

        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size,
                "state", state.name()
        );

        log.info("Обработка запроса арендующего на возврат аренд по bookerId = {} ", userId);
        return get("?from={from}&size={size}&state={state}", userId, parameters);
    }

    public ResponseEntity<Object> getAllByOwner(int from, int size, BookingState state, long userId) {
        if (state.equals(UNSUPPORTED_STATUS)) {
            throw new UnsupportedStatusException("Неподдерживаемый параметр BookingState");
        }

        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size,
                "state", state.name()
        );

        log.info("Обработка запроса владельца на возврат аренд по ownerId = {} ", userId);
        return get("/owner?from={from}&size={size}&state={state}", userId, parameters);
    }
}
