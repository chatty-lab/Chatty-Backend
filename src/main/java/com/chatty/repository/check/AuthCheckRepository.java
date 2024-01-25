package com.chatty.repository.check;

import com.chatty.entity.check.AuthCheck;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthCheckRepository extends JpaRepository<AuthCheck, Long> {

   Optional<AuthCheck> findAuthCheckByUserId(Long userId);
   void deleteAuthCheckByUserId(Long id);
}
