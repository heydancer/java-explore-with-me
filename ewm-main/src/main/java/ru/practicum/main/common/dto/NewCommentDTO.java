package ru.practicum.main.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewCommentDTO {
    @Size(min = 1, max = 3000)
    @NotBlank(message = "Field: text. Error: must not be blank. Value: null")
    private String text;
}
