package ru.practicum.ewm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.dto.ViewStats;
import ru.practicum.ewm.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<EndpointHit, Long> {
    @Query("SELECT new ru.practicum.ewm.dto.ViewStats(e.app, e.uri, COUNT(e.uri)) " +
            "FROM EndpointHit AS e " +
            "WHERE e.timestamp BETWEEN :start AND :end " +
            "GROUP BY e.app, e.uri " +
            "ORDER BY COUNT(e.uri) DESC")
    List<ViewStats> findViewStats(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.ewm.dto.ViewStats(e.app, e.uri, COUNT(e.uri)) " +
            "FROM EndpointHit AS e " +
            "WHERE e.timestamp BETWEEN :start AND :end " +
            "GROUP BY e.ip, e.app, e.uri " +
            "ORDER BY COUNT(e.uri) DESC")
    List<ViewStats> findViewStatsWithUniqIp(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.ewm.dto.ViewStats(e.app, e.uri, COUNT(e.uri)) " +
            "FROM EndpointHit AS e " +
            "WHERE e.timestamp BETWEEN :start AND :end " +
            "AND e.uri IN :uris " +
            "GROUP BY e.app, e.uri " +
            "ORDER BY COUNT(e.uri) DESC")
    List<ViewStats> findViewStatsWithUris(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("SELECT new ru.practicum.ewm.dto.ViewStats(e.app, e.uri, COUNT(e.uri)) " +
            "FROM EndpointHit AS e " +
            "WHERE e.timestamp BETWEEN :start AND :end " +
            "AND e.uri IN :uris " +
            "GROUP BY e.ip, e.app, e.uri " +
            "ORDER BY COUNT(e.uri) DESC")
    List<ViewStats> findViewStatsWithUrisAndUniqIp(LocalDateTime start, LocalDateTime end, List<String> uris);
}
