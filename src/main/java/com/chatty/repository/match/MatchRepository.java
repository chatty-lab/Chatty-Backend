package com.chatty.repository.match;

import com.chatty.entity.match.Match;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchRepository extends JpaRepository<Match, Long> {
}
