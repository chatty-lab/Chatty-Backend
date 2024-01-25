package com.chatty.entity.check;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthCheck {

    @Id
    @GeneratedValue
    private Long id;

    private Long userId;

    private Boolean checkNickname;

    private Boolean checkBirth;

    public void updateCheckNickname(Boolean value){
        this.checkNickname = value;
    }

    public void updateCheckBirth(Boolean value){
        this.checkBirth = value;
    }

    public static AuthCheck of(Long userId, Boolean checkNickname, Boolean checkBirth){
        return AuthCheck.builder()
                .userId(userId)
                .checkNickname(checkNickname)
                .checkBirth(checkBirth)
                .build();
    }
}
