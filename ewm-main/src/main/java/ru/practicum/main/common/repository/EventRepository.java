package ru.practicum.main.common.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.main.common.model.Event;
import ru.practicum.main.common.state.State;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findEventByIdAndInitiatorId(long eventId, long userId);

    List<Event> findEventsByInitiatorId(long userId, Pageable pageable);

    Optional<Event> findEventByIdAndState(long eventId, State state);

    @Query("SELECT e " +
            "FROM Event AS e " +
            "WHERE e.category.id = :categoryId")
    List<Event> findFirstByOrderByCategoryAsc(long categoryId, Pageable pageable);

    @Query("SELECT e " +
            "FROM Event AS e " +
            "WHERE e.id IN :eventIds")
    List<Event> findEventsByIds(List<Long> eventIds);

    @Query("SELECT e " +
            "FROM Event AS e " +
            "WHERE LOWER(e.annotation) LIKE %:text% OR LOWER(e.description) LIKE %:text% " +
            "AND (:categories is null OR e.category.id IN (:categories)) " +
            "AND e.paid = :paid " +
            "AND (:state is null OR e.state = :state) " +
            "AND (coalesce(:start, 'null') is null OR e.eventDate >= :start) " +
            "AND (coalesce(:end, 'null') is null OR e.eventDate <= :end)")
    List<Event> findPublicEvents(String text, List<Long> categories, boolean paid, LocalDateTime start,
                                 LocalDateTime end, State state, Pageable pageable);

    @Query("SELECT e " +
            "FROM Event AS e " +
            "WHERE (:users is null OR e.initiator.id IN (:users)) " +
            "AND (:states is null OR e.state IN (:states)) " +
            "AND (:categories is null OR e.category.id IN (:categories)) " +
            "AND (coalesce(:start, 'null') is null OR e.eventDate >= :start) " +
            "AND (coalesce(:end, 'null') is null OR e.eventDate <= :end) ")
    List<Event> findAdminEvents(List<Long> users, List<State> states, List<Long> categories,
                                LocalDateTime start, LocalDateTime end, Pageable pageable);
}
