package com.chatty.repository.user;

import com.chatty.entity.user.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByMobileNumber(String mobileNumber);

    Boolean existsUserByMobileNumber(String mobileNumber);

    Optional<User> findByNickname(String nickname);
}
