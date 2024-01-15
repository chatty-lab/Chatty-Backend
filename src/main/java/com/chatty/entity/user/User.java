package com.chatty.entity.user;

import com.chatty.constants.Authority;
import com.chatty.entity.CommonEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User  extends CommonEntity implements UserDetails{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "mobile_number")
    @NotBlank
    private String mobileNumber;

    @Column(name = "device_id")
    @NotBlank
    private String deviceId;

    private String nickname;

    private LocalDate birth;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private Mbti mbti;

//    @Column(columnDefinition = "POINT SRID 4326")
    private Point location;

    private String address;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    private String imageUrl;

    public void joinComplete(final User request) {
        this.nickname = request.getNickname();
        this.location = request.getLocation();
        this.gender = request.getGender();
        this.birth = request.getBirth();
        this.mbti = request.getMbti();
        this.authority = request.getAuthority();
    }

    public void updateNickname(final String nickname) {
        this.nickname = nickname;
    }

    public void updateGender(final Gender gender) {
        this.gender = gender;
    }

    public void updateBirth(final LocalDate birth) {
        this.birth = birth;
    }

    public void updateMbti(final Mbti mbti) {
        this.mbti = mbti;
    }

    public void updateCoordinate(final Coordinate coordinate) {
        this.location = createPoint(coordinate);
    }

    public void updateImage(final String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public static Point createPoint(final Coordinate coordinate) {
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        return geometryFactory.createPoint(new org.locationtech.jts.geom.Coordinate(coordinate.getLng(), coordinate.getLat()));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + authority.name()));
    }

    @Override
    public String getPassword() {
        return deviceId;
    }

    @Override
    public String getUsername() {
        return mobileNumber;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
