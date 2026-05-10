// 알림 설정 Entity
// notification_settings 테이블과 매핑
// 알림 설정 화면에서 설정한 세부 알림 정보 저장
// FCM 푸시 알림 발송 시 사용

package com.maeum.maeum.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "notification_settings")
@Getter
@Setter
@NoArgsConstructor
public class NotificationSettings {
    //알림 설정 고유 ID (자동 생성)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //유저 (users 테이블 연결, 유저당 하나의 알림 설정)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    //감정 기록 알림 ON/OFF
    @Column(nullable = false)
    private Boolean recordNotification = false;

    //감정 기록 알림 시간 (예: "21:00")
    @Column(nullable = false)
    private String notificationTime;

    //주간 마감 알림 ON/OFF
    @Column(nullable = false)
    private Boolean weeklyNotification = true;

    //월간 마감 알림 ON/OFF
    @Column(nullable = false)
    private Boolean monthlyNotification = true;
}
