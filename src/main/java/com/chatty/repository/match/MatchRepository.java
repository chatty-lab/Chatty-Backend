package com.chatty.repository.match;

import com.chatty.entity.match.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface MatchRepository extends JpaRepository<Match, Long> {

    @Query("select COUNT(*) " +
            "from Match m " +
            "where m.user.id = :userId and " +
            "m.registeredDateTime >= :startDateTime and m.registeredDateTime < :endDateTime " +
            "and m.isSuccess = :matchStatus")
    Long countMatchesBy(
            @Param("userId") Long userId,
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime,
            @Param("matchStatus") boolean matchStatus);
}
