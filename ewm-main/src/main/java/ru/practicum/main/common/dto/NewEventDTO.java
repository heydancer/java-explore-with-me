package ru.practicum.main.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import ru.practicum.main.common.model.Location;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewEventDTO {
    @NotNull(message = "Title cannot be null")
    @Size(min = 3, max = 120)
    private String title;

    @NotNull(message = "Annotation cannot be null")
    @Size(min = 20, max = 2000)
    private String annotation;

    @NotNull(message = "Description cannot be null")
    @Size(min = 20, max = 7000)
    private String description;

    @NotNull(message = "Category cannot be null")
    private Long category;

    @NotNull(message = "EventDate cannot be null")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    @NotNull(message = "Location cannot be null")
    private Location location;

    @NotNull(message = "Paid cannot be null")
    @Value("false")
    private Boolean paid;

    @NotNull(message = "ParticipantLimit cannot be null")
    @Value("0")
    private Long participantLimit;

    @NotNull(message = "RequestModeration cannot be null")
    @Value("false")
    private Boolean requestModeration;
}
