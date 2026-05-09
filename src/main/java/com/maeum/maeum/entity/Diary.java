// 일기 Entity
// diaries 테이블과 매핑
// 일기 화면에서 작성한 일기 저장
// AI 분석 시 당일 일기 내용 프롬프트에 반영

package com.maeum.maeum.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "diaries")
@Getter
@Setter
@NoArgsConstructor
public class Diary {
    //일기 고유 ID (자동 생성)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //작성한 유저 (users 테이블 연결)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    //일기 작성 날짜
    @Column(nullable = false)
    private LocalDate date;

    //일기 내용
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    //작성 시간
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    //저장 전 자동으로 작성 시간 설정
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
