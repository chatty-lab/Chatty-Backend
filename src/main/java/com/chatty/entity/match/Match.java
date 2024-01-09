package com.chatty.entity.match;

import com.chatty.entity.user.Gender;
import com.chatty.entity.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "Matches")
@Entity
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "match_id")
    private Long id;

    private int minAge;
    private int maxAge;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private Double scope;
    private String category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime registeredDateTime;
    private boolean isSuccess;

    @Builder
    public Match(final int minAge, final int maxAge, final Gender gender, final Double scope, final String category, final User user, final LocalDateTime registeredDateTime, final boolean isSuccess) {
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.gender = gender;
        this.scope = scope;
        this.category = category;
        this.user = user;
        this.registeredDateTime = registeredDateTime;
        this.isSuccess = isSuccess;
    }

    public void updateIsSuccess() {
        this.isSuccess = true;
    }
}
