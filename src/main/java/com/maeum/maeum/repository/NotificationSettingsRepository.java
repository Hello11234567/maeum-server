// NotificationSettings Repository
// notification_settings 테이블 조회/저장 인터페이스
// 알림 설정 조회/저장에 사용
// FCM 푸시 알림 발송 시 알림 설정 조회에 사용

package com.maeum.maeum.repository;

import com.maeum.maeum.entity.NotificationSettings;
import com.maeum.maeum.entity.User;
import org.aspectj.weaver.ast.Not;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationSettingsRepository extends JpaRepository<NotificationSettings, Long> {
    //유저의 알림 설정 조회 (알림 설정 화면 불러오기)
    Optional<NotificationSettings> findByUser(User user);

    //감정 기록 알림 ON인 유저 전체 조회 (FCM 알림 발송 시 사용)
    List<NotificationSettings> findByRecordNotificationTrue();

    //주간 마감 알림 ON인 유저 전체 조회 (FCM 알림 발송 시 사용)
    List<NotificationSettings> findByWeeklyNotificationTrue();

    //월간 마감 알림 ON인 유저 전체 조회 (FCM 알림 발송 시 사용)
    List<NotificationSettings> findByMonthlyNotificationTrue();
}
