package ru.practicum.main.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewCompilationDTO {
    @NotNull
    @NotBlank(message = "Field: title. Error: must not be blank. Value: null")
    private String title;

    @NotNull(message = "Pinned cannot be null")
    private Boolean pinned;

    @NotNull(message = "Events cannot be null")
    private List<Long> events;
}
