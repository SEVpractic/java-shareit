package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import ru.practicum.shareit.util.validation.CreateValidationGroup;
import ru.practicum.shareit.util.validation.UpdateValidationGroup;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Builder(toBuilder = true)
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemIncomeDto {
    @NotBlank(groups = CreateValidationGroup.class)
    @Size(max = 50, groups = {CreateValidationGroup.class, UpdateValidationGroup.class})
    private final String name;
    @NotBlank (groups = CreateValidationGroup.class)
    private final String description;
    @NotNull(groups = CreateValidationGroup.class)
    private final Boolean available;
    private final Long requestId;
}
