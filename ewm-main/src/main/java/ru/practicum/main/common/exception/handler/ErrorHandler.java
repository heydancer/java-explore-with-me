package ru.practicum.main.common.exception.handler;

import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.main.common.exception.ForbiddenException;
import ru.practicum.main.common.exception.NotFoundException;
import ru.practicum.service.utils.DateFormatter;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ErrorHandler {
    private static final Logger log = LoggerFactory.getLogger(ErrorHandler.class);

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValid(final MethodArgumentNotValidException exception) {
        log.error("Invalid arguments. {}", exception.getMessage());

        return new ErrorResponse("BAD_REQUEST", "Incorrectly made request.",
                exception.getMessage(), LocalDateTime.now().format(DateFormatter.FORMATTER));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(final NotFoundException exception) {
        log.error("Arguments not found. {}", exception.getMessage());

        return new ErrorResponse("NOT_FOUND", "The required object was not found.",
                exception.getMessage(), LocalDateTime.now().format(DateFormatter.FORMATTER));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConstraintViolationException(final ConstraintViolationException exception) {
        log.error("Category name cannot be changed. {}", exception.getMessage());

        return new ErrorResponse("CONFLICT", "Integrity constraint has been violated.",
                exception.getMessage(), LocalDateTime.now().format(DateFormatter.FORMATTER));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDataIntegrityViolationException(final DataIntegrityViolationException exception) {
        log.error("Category has a connection with other events. {}", exception.getMessage());

        return new ErrorResponse("CONFLICT", "For the requested operation the conditions are not met.",
                exception.getMessage(), LocalDateTime.now().format(DateFormatter.FORMATTER));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleForbiddenException(final ForbiddenException exception) {
        log.error("For the requested operation the conditions are not met. {}", exception.getMessage());

        return new ErrorResponse("FORBIDDEN", "For the requested operation the conditions are not met.",
                exception.getMessage(), LocalDateTime.now().format(DateFormatter.FORMATTER));
    }
}
