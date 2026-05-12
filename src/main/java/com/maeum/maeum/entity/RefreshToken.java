// Refresh Token Entity
// refresh_tokens 테이블과 매핑
// JWT Refresh Token 저장 및 관리
// Access Token 재발급 시 사용

package com.maeum.maeum.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "refresh_tokens")
@Getter
@Setter
@NoArgsConstructor
public class RefreshToken {
    //Refresh Token 고유 ID (자동 생성)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //유저 (users 테이블 연결)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    //Refresh Token 문자열
    @Column(nullable = false, unique = true, length = 500)
    private String token;

    //만료 시간
    @Column(nullable = false)
    private LocalDateTime expiryDate;

    //생성 시간
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    //저장 전 자동으로 생성 시간 설정
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
