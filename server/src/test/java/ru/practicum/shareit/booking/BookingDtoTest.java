package ru.practicum.shareit.booking;

import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.Optional;

@JsonTest
class BookingDtoTest {
    @Autowired
    private JacksonTester<BookingDto> json;

    @Test
    @SneakyThrows
    void bookingDtoTest() {
        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = start.plusHours(1);
        BookingDto bookingDto = BookingDto.builder()
                .id(1L)
                .start(start)
                .end(end)
                .item(new BookingDto.ShortItemDto(1L, "itemName"))
                .booker(new BookingDto.ShortBookerDto(1L, "userName"))
                .status(BookingStatus.APPROVED)
                .build();

        Optional<JsonContent<BookingDto>> result = Optional.of(json.write(bookingDto));

        Assertions.assertThat(result)
                .isPresent()
                .hasValueSatisfying(i -> {
                    Assertions.assertThat(i)
                            .extractingJsonPathNumberValue("$.id").isEqualTo(1);
                    Assertions.assertThat(i)
                            .extractingJsonPathStringValue("$.start");
                    Assertions.assertThat(i)
                            .extractingJsonPathStringValue("$.end");
                    Assertions.assertThat(i)
                            .extractingJsonPathStringValue("$.status").isEqualTo("APPROVED");
                    Assertions.assertThat(i)
                            .hasJsonPathValue("$.item")
                            .extractingJsonPathNumberValue("$.item.id").isEqualTo(1);
                    Assertions.assertThat(i)
                            .hasJsonPathValue("$.item")
                            .extractingJsonPathStringValue("$.item.name").isEqualTo("itemName");
                    Assertions.assertThat(i)
                            .hasJsonPathValue("$.booker")
                            .extractingJsonPathNumberValue("$.booker.id").isEqualTo(1);
                    Assertions.assertThat(i)
                            .hasJsonPathValue("$.booker")
                            .extractingJsonPathStringValue("$.booker.name").isEqualTo("userName");
                });
    }
}
