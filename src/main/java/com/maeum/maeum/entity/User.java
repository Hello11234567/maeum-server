// 사용자 정보 Entity
// users 테이블과 매핑
// 카카오 로그인 후 사용자 정보 저장
// 마이페이지, 프로필 수정, AI 분석 개인화에 사용

package com.maeum.maeum.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {
    //사용자 고유 ID (자동 생성)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //카카오 고유 ID (로그인 시 카카오에서 받아옴)
    @Column(nullable = false, unique = true)
    private String kakaoId;

    //닉네임 (카카오에서 자동 가져오거나 프로필 수정에서 변경)
    @Column(nullable = false, length = 50)
    private String nickname;

    // 프로필 이미지 URL (프로필 수정에서 업로드)
    @Column(length = 500)
    private String profileImage;

    //나를 한 마디로 (AI 분석 프롬프트에 반영)
    @Column(length = 100)
    private String intro;

    // 나이대 (AI 분석 프롬프트에 반영)
    @Column(length = 20)
    private String ageRange;

    //알림 설정 (ON/OFF)
    @Column(nullable = false)
    private Boolean notificationsEnabled = false;

    //가입일 (마음이와 함께한 N일 계산에 사용)
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    //저장 전 자동으로 가입일 설정
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
