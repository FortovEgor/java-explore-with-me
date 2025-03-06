package ru.practicum.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.Category;
import ru.practicum.category.CategoryService;
import ru.practicum.client.EventStatClient;
import ru.practicum.event.dto.*;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;
import ru.practicum.event.model.Location;
import ru.practicum.exception.IncorrectInputDataException;
import ru.practicum.exception.IncorrectActionException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.User;
import ru.practicum.user.UserService;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.event.model.EventState.*;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EventService {

    private final UserService userService;
    private final CategoryService categoryService;
    private final EventRepository repo;
    private final EventMapper mapper;
    private final EventStatClient client;

    @Transactional
    public Event createEvent(CreateEventRequest request, long userId) {
        log.info("creating event {} by user wit id = {}", request, userId);

        User user = userService.getUsersById(userId);
        Category category = categoryService.getCategoryById(request.getCategory());

        Event event = mapper.toEvent(request);
        event.setInitiator(user);
        event.setCategory(category);

        log.info("saving event {} to DB", event);
        return repo.save(event);
    }

    public List<Event> getEventsByUser(long userId, long from, long size) {
        log.info("getting all user events(userId: {}, from: {}, size: {}", userId, from, size);
        User user = userService.getUsersById(userId);
        return repo.findAllByUserWithFromAndOfSize(user, from, size);
    }

    public List<Event> findEvents(List<Long> userIds, List<String> stateNames, List<Long> categoryIds,
                                         LocalDateTime rangeStart, LocalDateTime rangeEnd, Long from, Long size) {
        log.info("finding events(userIds = {}, stateNames = {}, categoryIds = {}, rangeStart = {}, rangeEnd = {}, from = {}, size = {})",
                userIds, stateNames, categoryIds, rangeStart, rangeEnd, from, size);

        if (stateNames != null && stateNames.isEmpty()) {
            return List.of();
        }

        if (categoryIds != null && categoryIds.isEmpty()) {
            return List.of();
        }

        if (userIds != null && userIds.isEmpty()) {
            return List.of();
        }

        if (rangeStart != null && rangeEnd != null && rangeStart.isAfter(rangeEnd)) {
            throw new IncorrectInputDataException("Задан rangeEnd до rangeStart");
        }

        if (from < 0 || size <= 0) {
            return List.of();
        }

        List<Category> categories = getAllCategoriesById(categoryIds);
        List<EventState> states = getAllEventStateById(stateNames);
        List<User> users = getAllUsersById(userIds);

        return repo.findAllByParams(userIds == null, users,
                categoryIds == null, categories,
                stateNames == null, states,
                rangeStart == null, rangeStart,
                rangeEnd == null, rangeEnd,
                from, size);
    }

    @Transactional
    public Event updateEventByAdmin(long eventId, UpdateEventRequest request) {
        log.info("updating event with id = {}, request: {}", eventId, request);
        Event event = getFullEventDataById(eventId);

        if (request.getStateAction() != null) {

            EventStateAction stateAction = EventStateAction.valueOf(request.getStateAction());
            EventState state = switch (stateAction) {
                case EventStateAction.PUBLISH_EVENT -> EventState.PUBLISHED;
                case EventStateAction.REJECT_EVENT -> EventState.CANCELED;
                default -> throw new IncorrectInputDataException("Запрещенный stateAction " + stateAction);
            };

            event.setState(state);
        }

        return updateEvent(event, request);
    }

    @Transactional
    public Event updateEventByUser(long userId, long eventId, UpdateEventRequest request) {
        log.info("updating event with id = {} by user with id = {}, request: {}",
                eventId, userId, request);
        User user = userService.getUsersById(userId);
        Event event = getFullEventDataById(eventId);

        if (!event.getInitiator().equals(user)) {
            throw new NotFoundException("Событие с id = " + eventId + " не найдено.");
        }

        if (event.getState() == EventState.PUBLISHED) {
            throw new IncorrectActionException("Нельзя изменить опубликованное событие");
        }

        if (request.getStateAction() != null) {

            EventStateAction stateAction = EventStateAction.valueOf(request.getStateAction());
            EventState state = switch (stateAction) {
                case EventStateAction.SEND_TO_REVIEW -> EventState.PENDING;
                case EventStateAction.CANCEL_REVIEW -> EventState.CANCELED;
                default -> throw new IncorrectInputDataException("Запрещенный stateAction " + stateAction);
            };

            event.setState(state);
        }

        return updateEvent(event, request);
    }

    @Transactional
    private Event updateEvent(Event event, UpdateEventRequest request) {
        if (request.getTitle() != null) {
            event.setTitle(request.getTitle());
        }

        if (request.getEventDate() != null) {
            event.setEventDate(request.getEventDate());
        }

        if (request.getAnnotation() != null) {
            event.setAnnotation(request.getAnnotation());
        }

        if (request.getDescription() != null) {
            event.setDescription(request.getDescription());
        }

        if (request.getLocation() != null) {
            Location location = mapper.toLocation(request.getLocation());
            event.setLocation(location);
        }

        if (request.getCategory() != null) {
            Category category = categoryService.getCategoryById(request.getCategory());
            event.setCategory(category);
        }

        if (request.getRequestModeration() != null) {
            event.setRequestModeration(request.getRequestModeration());
        }

        if (request.getPaid() != null) {
            event.setPaid(request.getPaid());
        }

        if (request.getParticipantLimit() != null) {
            event.setParticipantLimit(request.getParticipantLimit());
        }

        log.info("saving event {} to DB");
        return repo.save(event);
    }

    public Event getEvent(long userId, long eventId) {
        log.info("getting event with id {} by user with id = {}", eventId, userId);
        User user = userService.getUsersById(userId);
        Event event = getFullEventDataById(eventId);

        if (!event.getInitiator().equals(user)) {
            throw new NotFoundException("Событие с id = " + eventId + " не найдено.");
        }

        return event;
    }

    public List<Event> searchEvents(String text, List<Long> categoryIds, Boolean paid, LocalDateTime rangeStart,
                                    LocalDateTime rangeEnd, boolean onlyAvailable, EventSearchSort sort,
                                    long from, long size) {
        log.info("searching events( text {}, categoryIds {}, paid {}, rangeStart {}, rangeEnd {}, " +
                        "onlyAvailable {}, sort {}. from {}, size {})",
                text, categoryIds, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);

        if (categoryIds != null && categoryIds.isEmpty()) {
            return List.of();
        }

        if (rangeStart != null && rangeEnd != null && rangeStart.isAfter(rangeEnd)) {
            throw new IncorrectInputDataException("Задан rangeEnd до rangeStart");
        }

        if (rangeStart == null && rangeEnd == null) {
            rangeStart = LocalDateTime.now();
        }

        if (from < 0 || size <= 0) {
            return List.of();
        }

        String normalizedText = getNormalizedText(text);
        List<Category> categories = getAllCategoriesById(categoryIds);

        List<Event> events = repo.getByAllOtherParams(
                text == null, normalizedText,
                categoryIds == null, categories,
                paid == null, paid,
                rangeStart == null, rangeStart,
                rangeEnd == null, rangeEnd);

        if (onlyAvailable) {
            events = events.stream()
                    .filter(e -> e.getParticipantLimit() != 0 && e.getConfirmedRequests() < e.getParticipantLimit())
                    .collect(Collectors.toList());
        }

        client.setHits(events);

        Comparator<Event> comp = switch (sort) {
            case EVENT_DATE -> Comparator.comparing(Event::getEventDate);
            case VIEWS -> Comparator.comparing(Event::getViews);
        };

        return events.stream()
                .sorted(comp)
                .skip(from)
                .limit(size)
                .toList();
    }

    public Event getPublishedEventById(long eventId) {
        log.info("getting published event with id = {}", eventId);
        Event event = getFullEventDataById(eventId);
        if (event.getState() != PUBLISHED) {
            throw new NotFoundException("Событие с id = " + eventId + " не найдено.");
        }

        return event;
    }

    private String getNormalizedText(String text) {
        return text == null ? null : text.toLowerCase().trim();
    }

    private List<User> getAllUsersById(List<Long> ids) {
        return ids == null ? List.of() : userService.getUsersById(ids);
    }

    private List<Category> getAllCategoriesById(List<Long> ids) {
        return ids == null ? List.of() : categoryService.getCategoriesById(ids);
    }

    private List<EventState> getAllEventStateById(List<String> stateNames) {
        return stateNames == null ? List.of() : EventState.getByNames(stateNames);
    }

    public Event getEventById(long eventId) {
        log.info("getting event with id = {}", eventId);
        return repo.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие с id = " + eventId + " не найдено."));
    }

    public Event getFullEventDataById(long eventId) {
        log.info("getting full event data (eventId = {})", eventId);
        Event event = repo.getAllDataByEventId(eventId)
                .orElseThrow(() -> new NotFoundException("Событие с id = " + eventId + " не найдено."));
        client.setHits(event);

        return event;
    }

    public List<Event> getAllFullById(List<Long> eventIds) {
        log.info("getting all events full data with ids = {}", eventIds);
        List<Event> events = repo.getAllDataByEventsIds(eventIds);
        client.setHits(events);
        return events;
    }
}
