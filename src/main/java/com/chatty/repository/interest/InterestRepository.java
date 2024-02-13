package com.chatty.repository.interest;

import com.chatty.entity.user.Interest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterestRepository extends JpaRepository<Interest, Long> {
}
