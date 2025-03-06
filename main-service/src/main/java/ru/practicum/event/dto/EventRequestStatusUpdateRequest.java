package ru.practicum.event.dto;

import lombok.Data;
import ru.practicum.participation.model.ParticipationStatus;

import java.util.List;

@Data
public class EventRequestStatusUpdateRequest {
    private List<Long> requestIds;

    private ParticipationStatus status;
}
