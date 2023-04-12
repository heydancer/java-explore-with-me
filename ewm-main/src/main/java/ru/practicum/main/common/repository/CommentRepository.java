package ru.practicum.main.common.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.main.common.model.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findCommentByIdAndAuthorId(long commentId, long userId);

    List<Comment> findCommentsByAuthorId(long userId, Pageable pageable);

    List<Comment> findCommentsByAuthorIdAndEventId(long userId, long eventId, Pageable pageable);

    List<Comment> findCommentsByEventId(long eventId, Pageable pageable);

    @Query("SELECT c " +
            "FROM Comment AS c " +
            "WHERE (:userId is null OR c.author.id=:userId) " +
            "AND (:eventId is null OR c.event.id=:eventId)")
    List<Comment> findAdminComments(Long userId, Long eventId, Pageable pageable);
}
