// 감정 수치 Entity
// emotion_records 테이블과 매핑
// AI 분석 화면에서 입력한 감정 수치 저장
// 통계 화면, AI 분석 프롬프트에 사용

package com.maeum.maeum.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "emotion_records")
@Getter
@Setter
@NoArgsConstructor
public class EmotionRecord {
    //감정 기록 고유 ID (자동 생성)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //작성한 유저 (users 테이블 연결)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    //기록 날짜
    @Column(nullable = false)
    private LocalDate date;

    //기쁨 수치 (0-10)
    @Column(nullable = false)
    private Double joy = 5.0;

    //화남 수치 (0-10)
    @Column(nullable = false)
    private Double anger = 5.0;

    //불안 수치 (0-10)
    @Column(nullable = false)
    private Double anxiety = 5.0;

    //평안 수치 (0-10)
    @Column(nullable = false)
    private Double peace = 5.0;

    //슬픔 수치 (0-10)
    @Column(nullable = false)
    private Double sadness = 5.0;

    //내가 선택한 이모지 (캘린더에 표시)
    @Column(length = 10)
    private String myEmoji;

    //AI가 선정한 대표 이모지 (캘린더에 표시)
    @Column(nullable = false)
    private String aiEmoji;

    //마지막 수정 시간 (AI 분석 결과 화면에 표시)
    @Column
    private LocalDateTime lastModified;

    //저장/수정 시 자동으로 마지막 수정 시간 설정
    @PrePersist
    @PreUpdate
    public void preUpdate() {
        this.lastModified = LocalDateTime.now();
    }
}
