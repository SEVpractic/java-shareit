package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingIncomeDto;
import ru.practicum.shareit.booking.impl.BookingController;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingControllerTest {
    private final ObjectMapper objectMapper;
    private final MockMvc mockMvc;
    @MockBean
    private BookingService bookingService;

    @SneakyThrows
    @Test
    void getById_correctUser_thenReturnOk() {
        long userId = 1L;
        long bookingId = 1L;
        mockMvc.perform(get("/bookings/{bookingId}", bookingId)
                        .header("X-Sharer-User-Id", userId))
                .andDo(print())
                .andExpect(status().isOk());

        verify(bookingService).getById(bookingId, userId);
    }

    @SneakyThrows
    @Test
    void getAllByBooker_notWalidPagination_thenReturnOk() {
        long userId = 1L;
        int from = -1;
        int size = 0;
        mockMvc.perform(get("/bookings?from={from}&size={size}", from, size)
                        .header("X-Sharer-User-Id", userId))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(bookingService, never()).getAllByBooker(0, 10, BookingState.ALL, 1L);
    }

    @SneakyThrows
    @Test
    void create_allCorrect_thenReturnOk() {
        long userId = 1L;
        BookingIncomeDto bookingIncomeDto = BookingIncomeDto.builder()
                .itemId(1L)
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusHours(2))
                .build();
        BookingDto bookingDto = BookingDto.builder()
                .id(1L)
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusHours(2))
                .booker(new BookingDto.ShortBookerDto(1L, "user"))
                .item(new BookingDto.ShortItemDto(1L, "item"))
                .status(BookingStatus.WAITING)
                .build();

        when(bookingService.create(any(), anyLong())).thenReturn(bookingDto);
        String content = mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingIncomeDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Assertions.assertEquals(objectMapper.writeValueAsString(bookingDto), content);
    }

    @SneakyThrows
    @Test
    void confirm_allCorrect_thenReturnOk() {
        long userId = 1L;
        long bookingId = 1L;
        boolean approved = true;
        mockMvc.perform(patch("/bookings/{bookingId}?approved={approved}", bookingId, approved)
                        .header("X-Sharer-User-Id", userId))
                .andDo(print())
                .andExpect(status().isOk());

        verify(bookingService).confirm(bookingId, userId, approved);
    }
}
