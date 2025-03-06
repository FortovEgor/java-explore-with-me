package ru.practicum.participation;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.participation.dto.ParticipationRequest;
import ru.practicum.participation.model.Participation;

import java.util.List;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ParticipationMapper {

    @Mapping(target = "event", source = "event.id")
    @Mapping(target = "requester", source = "requester.id")
    ParticipationRequest toDto(Participation participation);

    List<ParticipationRequest> toDto(List<Participation> participations);
}
