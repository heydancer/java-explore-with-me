package ru.practicum.ewm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.practicum.ewm.dto.ViewStats;
import ru.practicum.ewm.dto.EndpointHitDTO;

import java.util.List;

@Service
public class StatsClient {
    private final WebClient webClient;

    @Autowired
    public StatsClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<Void> addHit(EndpointHitDTO endpointHitDTO) {
        return webClient
                .post()
                .uri("/hit")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(endpointHitDTO), EndpointHitDTO.class)
                .retrieve()
                .bodyToMono(Void.class);
    }

    public Mono<List<ViewStats>> getStats(String start, String end, List<String> uris, Boolean unique) {
        String paramsUri = uris.stream()
                .reduce("", (result, uri) -> result + "&uris=" + uri);

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/stats")
                        .queryParam("start", start)
                        .queryParam("end", end)
                        .query(paramsUri)
                        .queryParam("unique", unique)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<ViewStats>>() {
                });
    }
}
