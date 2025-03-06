package ru.practicum.event.model;

import java.util.List;

public enum EventState {
    PENDING,
    PUBLISHED,
    CANCELED;

    public static List<EventState> getByNames(List<String> names) {
        return names.stream().map(EventState::valueOf).toList();
    }
}
