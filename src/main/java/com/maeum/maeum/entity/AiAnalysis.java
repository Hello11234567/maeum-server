// AI 분석 결과 Entity
// ai_analyses 테이블과 매핑
// AI 분석 결과 저장
// AI 분석 결과 화면, 캘린더 지난 결과 보기에 사용

package com.maeum.maeum.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "ai_analyses")
@Getter
@Setter
@NoArgsConstructor
public class AiAnalysis {
    //AI 분석 고유 ID (자동 생성)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //분석한 유저 (users 테이블 연결)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    //분석 날짜
    @Column(nullable = false)
    private LocalDate date;

    //AI가 생성한 하루 요약
    @Column(columnDefinition = "TEXT")
    private String summary;

    //AI 감정 케어 추천 목록 (JSON 형태로 저장)
    @Column(columnDefinition = "TEXT")
    private String careList;

    //캐릭터 말풍선 텍스트
    @Column(length = 255)
    private String speechText;

    //AI가 선정한 대표 이모지 (캘린더에 표시)
    @Column(length = 10)
    private String representativeEmoji;

    //마지막 수정 시간 (AI 분석 결과 화면 상단에 표시)
    @Column
    private LocalDateTime lastModified;

    //저장/수정 시 자동으로 마지막 수정 시간 설정
    @PrePersist
    @PreUpdate
    public void preUpdate() {
        this.lastModified = LocalDateTime.now();
    }
}
