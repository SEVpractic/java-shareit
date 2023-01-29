package ru.practicum.shareit.booking.model;

public enum BookingState {
    ALL, // все
    CURRENT, // текущие
    PAST, // завершённые
    FUTURE, // будущие
    WAITING, // ожидающие подтверждения
    REJECTED, // отклонённые
    UNSUPPORTED_STATUS; // неподдерживаемый статус
}
