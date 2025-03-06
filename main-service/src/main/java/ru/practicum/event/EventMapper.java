package ru.practicum.event;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.category.CategoryMapper;
import ru.practicum.event.dto.EventDto;
import ru.practicum.event.dto.EventForCompilationDto;
import ru.practicum.event.dto.LocationDto;
import ru.practicum.event.dto.CreateEventRequest;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.Location;
import ru.practicum.user.UserMapper;

import java.util.List;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {UserMapper.class, CategoryMapper.class})
public interface EventMapper {
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "initiator", ignore = true)
    Event toEvent(CreateEventRequest dto);

    Location toLocation(LocationDto dto);

    EventDto toEventDto(Event event);

    List<EventDto> toEventDto(List<Event> events);

    List<EventForCompilationDto> toEventForCompilationDto(List<Event> events);
}
