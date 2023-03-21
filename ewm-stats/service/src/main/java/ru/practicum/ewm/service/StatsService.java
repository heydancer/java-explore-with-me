package ru.practicum.ewm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.ViewStats;
import ru.practicum.ewm.repository.StatsRepository;
import ru.practicum.ewm.utils.DateTimeCoder;
import ru.practicum.ewm.dto.EndpointHitDTO;
import ru.practicum.ewm.mapper.EndpointHitMapper;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StatsService {
    private final StatsRepository statsRepository;
    private final EndpointHitMapper mapper;

    @Autowired
    public StatsService(StatsRepository statsRepository, EndpointHitMapper mapper) {
        this.statsRepository = statsRepository;
        this.mapper = mapper;
    }

    public void addEndpointHit(EndpointHitDTO endpointHitDTO) {
        statsRepository.save(mapper.toModel(endpointHitDTO));
    }

    public List<ViewStats> getStats(String start, String end, List<String> uris, Boolean unique) {
        LocalDateTime[] localDateTimes = DateTimeCoder.decoder(start, end);

        if (!unique) {
            if (uris == null) {
                return statsRepository.findViewStats(localDateTimes[0], localDateTimes[1]);
            } else {
                return statsRepository.findViewStatsWithUris(localDateTimes[0], localDateTimes[1], uris);
            }

        } else {
            if (uris == null) {
                return statsRepository.findViewStatsWithUniqIp(localDateTimes[0], localDateTimes[1]);
            } else {
                return statsRepository.findViewStatsWithUrisAndUniqIp(localDateTimes[0], localDateTimes[1], uris);
            }
        }
    }
}
