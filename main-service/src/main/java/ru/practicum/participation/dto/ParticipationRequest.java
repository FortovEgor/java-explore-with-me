package ru.practicum.participation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.practicum.participation.model.ParticipationStatus;

import java.time.LocalDateTime;

@Data
public class ParticipationRequest {
    private Long id;

    private Long event;

    private Long requester;

    private ParticipationStatus status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;
}