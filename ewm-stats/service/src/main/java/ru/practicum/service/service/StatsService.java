package ru.practicum.service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.service.mapper.EndpointHitMapper;
import ru.practicum.dto.ViewStats;
import ru.practicum.service.utils.DateFormatter;
import ru.practicum.service.repository.StatsRepository;
import ru.practicum.dto.EndpointHitDTO;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class StatsService {
    private final StatsRepository statsRepository;
    private final EndpointHitMapper mapper;

    @Autowired
    public StatsService(StatsRepository statsRepository, EndpointHitMapper mapper) {
        this.statsRepository = statsRepository;
        this.mapper = mapper;
    }

    @Transactional
    public void addEndpointHit(EndpointHitDTO endpointHitDTO) {
        statsRepository.save(mapper.toModel(endpointHitDTO));
    }

    public List<ViewStats> getStats(String start, String end, List<String> uris, Boolean unique) {
        LocalDateTime[] localDateTimes = DateFormatter.decoder(start, end);

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
