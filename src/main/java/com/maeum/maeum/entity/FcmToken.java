//FCM 토큰 Entity
//fcm_tokens 테이블과 매핑
//푸시 알림 발송 시 사용

package com.maeum.maeum.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.checkerframework.checker.units.qual.C;

import java.time.LocalDateTime;

@Entity
@Table(name = "fcm_tokens")
@Getter
@Setter
@NoArgsConstructor
public class FcmToken {
    //fcm 고유 ID (자동생성)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //유저 (users 테이블 연결)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    //FCM 토큰
    @Column(nullable = false, length = 500)
    private String token;

    //생성 시간
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    //수정 시간
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    //저장/수정 시 자동으로 시간 설정
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
