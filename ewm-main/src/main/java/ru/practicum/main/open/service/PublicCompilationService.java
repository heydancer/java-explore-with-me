package ru.practicum.main.open.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.common.dto.CompilationDTO;
import ru.practicum.main.common.exception.NotFoundException;
import ru.practicum.main.common.mapper.CompilationMapper;
import ru.practicum.main.common.model.Compilation;
import ru.practicum.main.common.repository.CompilationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PublicCompilationService {
    private final CompilationRepository compilationRepository;
    private final CompilationMapper compilationMapper;

    public List<CompilationDTO> getCompilations(Boolean pinned, Pageable pageable) {
        if (pinned == null) {
            return compilationRepository.findAll(pageable).stream()
                    .map(compilationMapper::toCompilationDTO)
                    .collect(Collectors.toList());
        } else {
            return compilationRepository.findAllByPinned(pinned, pageable).stream()
                    .map(compilationMapper::toCompilationDTO)
                    .collect(Collectors.toList());
        }
    }

    public CompilationDTO getCompilationById(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Failed to find compilation with id=%d", compId)));

        return compilationMapper.toCompilationDTO(compilation);
    }
}
