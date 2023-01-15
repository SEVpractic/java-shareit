package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import ru.practicum.shareit.util.validation.CreateValidationGroup;

import javax.validation.constraints.NotBlank;

@Builder(toBuilder = true)
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemRequestIncomeDto {
    @NotBlank (groups = CreateValidationGroup.class) // todo переделать без групп, если дальше не нужны
    private final String description;
    private Long id; // todo понять почему нельзя делать дто из одной строки
}
