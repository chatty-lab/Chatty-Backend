package com.chatty.repository.profileUnlock;

import com.chatty.entity.user.ProfileUnlock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileUnlockRepository extends JpaRepository<ProfileUnlock, Long> {

    boolean existsByUnlockerIdAndUnlockedUserId(Long unlockerId, Long unlockedUserId);
}
