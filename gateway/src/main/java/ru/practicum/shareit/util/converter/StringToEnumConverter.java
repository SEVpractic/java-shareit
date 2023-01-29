package ru.practicum.shareit.util.converter;

import org.springframework.core.convert.converter.Converter;
import ru.practicum.shareit.booking.dto.BookingState;

public class StringToEnumConverter implements Converter<String, BookingState> {
    @Override
    public BookingState convert(String source) {
        try {
            return BookingState.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException ex) {
            return BookingState.UNSUPPORTED_STATUS;
        }
    }
}
