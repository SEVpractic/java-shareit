package ru.practicum.shareit.util;

public enum BookingStatus {
    WAITING, // новое бронирование, ожидает одобрения,
    APPROVED, // бронирование подтверждено владельцем
    REJECTED, // бронирование отклонено владельцем
    CANCELED; // бронирование отменено создателем
}
