package com.chatty.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import org.springframework.data.geo.Point;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue
    private Long id;

    private String username;

    private String nickname;

    private LocalDate birth;

    private String mbti;

    private Point location;

    private String address;

    private String uuid;
}

