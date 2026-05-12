// 알림 서비스
// FCM 푸시 알림 발송
// 알림 설정 저장/조회
// 감정 기록 알림, 주간/월간 마감 알림

package com.maeum.maeum.service;

import com.maeum.maeum.dto.request.NotificationSettingsRequest;
import com.maeum.maeum.dto.response.NotificationSettingsResponse;
import com.maeum.maeum.entity.NotificationSettings;
import com.maeum.maeum.entity.User;
import com.maeum.maeum.repository.NotificationSettingsRepository;
import com.maeum.maeum.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationSettingsRepository notificationSettingsRepository;
    private final UserRepository userRepository;

    //알림 설정 조회
    public NotificationSettingsResponse getNotificationSettings(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

        NotificationSettings settings = notificationSettingsRepository
                .findByUser(user)
                .orElseGet(() -> {
                    //알림 설정 없으면 기본값으로 생성
                    NotificationSettings newSettings = new NotificationSettings();
                    newSettings.setUser(user);
                    newSettings.setRecordNotification(false);
                    newSettings.setWeeklyNotification(true);
                    newSettings.setMonthlyNotification(true);
                    return notificationSettingsRepository.save(newSettings);
                });

        return new NotificationSettingsResponse(
                settings.getRecordNotification(),
                settings.getNotificationTime(),
                settings.getWeeklyNotification(),
                settings.getMonthlyNotification()
        );
    }

    //알림 설정 저장
    @Transactional
    public NotificationSettingsResponse saveNotificationSetings(Long userId, NotificationSettingsRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

        NotificationSettings settings = notificationSettingsRepository
                .findByUser(user)
                .orElse(new NotificationSettings());

        settings.setUser(user);
        settings.setRecordNotification(request.getRecordNotification());
        settings.setWeeklyNotification(request.getWeeklyNotification());
        settings.setMonthlyNotification(request.getMonthlyNotification());

        NotificationSettings saved = notificationSettingsRepository.save(settings);

        return new NotificationSettingsResponse(
                saved.getRecordNotification(),
                saved.getNotificationTime(),
                saved.getWeeklyNotification(),
                saved.getMonthlyNotification()
        );
    }

    //FCM 푸시 알림 발송 (나중에 구현)
    //1. 감정 기록 알림: 매분마다 체크, 설정 시간 맞으면 발송
    //2. 주간 마감 알림: 매주 일요일 밤 11:30
    //3. 월간 마감 알림: 매월 말일 밤 11:30
    public void sendRecordNotification() {
        //FCM 연동 시 구현
    }

    public void sendWeeklyNotification() {
        //FCM 연동 시 구현
    }

    public void sendMonthlyNotification() {
        //FCM 연동 시 구현
    }
}
