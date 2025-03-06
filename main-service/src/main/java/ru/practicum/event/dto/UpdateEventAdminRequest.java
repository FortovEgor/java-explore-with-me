package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.practicum.validator.FutureCustom;

import java.time.LocalDateTime;

@Data
public class UpdateEventAdminRequest implements UpdateEventRequest {
    @Size(min = 3, max = 120, message = "Название события должно быть не короче 3 и не длинее 120 символов")
    private String title;

    @Size(min = 20, max = 2000, message = "Аннотация события должно быть не короче 20 и не длинее 2000 символов")
    private String annotation;

    private Long category;

    @Size(min = 20, max = 7000, message = "Описание события должно быть не короче 20 и не длинее 7000 символов")
    private String description;

    @FutureCustom(seconds = 60 * 60)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private LocationDto location;

    private Boolean paid;

    @PositiveOrZero
    private Integer participantLimit;

    private Boolean requestModeration;

    private String stateAction;
}
