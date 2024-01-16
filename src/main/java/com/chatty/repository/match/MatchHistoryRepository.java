package com.chatty.repository.match;

import com.chatty.entity.match.MatchHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchHistoryRepository extends JpaRepository<MatchHistory, Long> {
}
