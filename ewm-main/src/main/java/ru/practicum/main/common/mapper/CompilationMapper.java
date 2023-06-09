package ru.practicum.main.common.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.main.common.dto.CompilationDTO;
import ru.practicum.main.common.dto.NewCompilationDTO;
import ru.practicum.main.common.model.Compilation;
import ru.practicum.main.common.model.Event;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CompilationMapper {
    private final EventMapper eventMapper;

    public CompilationDTO toCompilationDTO(Compilation compilation) {
        return CompilationDTO.builder()
                .id(compilation.getId())
                .title(compilation.getTitle())
                .pinned(compilation.getPinned())
                .events(compilation.getEvents().stream()
                        .map(eventMapper::toEventShortDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    public Compilation toCompilation(NewCompilationDTO newCompilationDTO, Set<Event> events) {
        return Compilation.builder()
                .title(newCompilationDTO.getTitle())
                .pinned(newCompilationDTO.getPinned())
                .events(events)
                .build();
    }
}
