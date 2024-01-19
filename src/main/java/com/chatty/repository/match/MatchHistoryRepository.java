package com.chatty.repository.match;

import com.chatty.entity.match.MatchHistory;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MatchHistoryRepository extends JpaRepository<MatchHistory, Long> {

    boolean existsBySenderIdAndReceiverId(Long senderId, Long receiverId);

}
