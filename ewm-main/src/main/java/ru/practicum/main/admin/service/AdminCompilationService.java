package ru.practicum.main.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.common.dto.CompilationDTO;
import ru.practicum.main.common.dto.NewCompilationDTO;
import ru.practicum.main.common.dto.UpdateCompilationRequest;
import ru.practicum.main.common.exception.ForbiddenException;
import ru.practicum.main.common.exception.NotFoundException;
import ru.practicum.main.common.mapper.CompilationMapper;
import ru.practicum.main.common.model.Compilation;
import ru.practicum.main.common.model.Event;
import ru.practicum.main.common.repository.CompilationRepository;
import ru.practicum.main.common.repository.EventRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminCompilationService {
    private final CompilationRepository compilationRepository;
    private final CompilationMapper compilationMapper;
    private final EventRepository eventRepository;

    @Transactional
    public CompilationDTO addCompilation(NewCompilationDTO newCompilationDTO) {
        List<Long> eventIds = newCompilationDTO.getEvents();
        Set<Event> events = new HashSet<>(eventRepository.findEventsByIds(eventIds));

        Compilation compilation = compilationMapper.toCompilation(newCompilationDTO, events);

        return compilationMapper.toCompilationDTO(compilationRepository.save(compilation));
    }

    @Transactional
    public CompilationDTO updateCompilation(Long compId, UpdateCompilationRequest updateCompilationRequest) {
        Compilation compilation = checkCompilation(compId);

        List<Long> eventIds = updateCompilationRequest.getEvents();
        Set<Event> events;

        if (updateCompilationRequest.getTitle() != null)
            compilation.setTitle(updateCompilationRequest.getTitle());

        if (updateCompilationRequest.getPinned() != null)
            compilation.setPinned(updateCompilationRequest.getPinned());

        if (eventIds.isEmpty()) {
            throw new ForbiddenException("Field: events. Error: must not be empty. Value: 0");
        } else {
            events = new HashSet<>(eventRepository.findEventsByIds(eventIds));
            compilation.getEvents().clear();
            compilation.setEvents(events);
        }

        return compilationMapper.toCompilationDTO(compilation);
    }

    @Transactional
    public void deleteById(Long compId) {
        Compilation compilation = checkCompilation(compId);
        compilationRepository.delete(compilation);
    }

    private Compilation checkCompilation(long catId) {
        return compilationRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Compilation with id=%s was not found", catId)));
    }
}
