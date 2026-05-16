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
import com.maeum.maeum.repository.FcmTokenRepository;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationSettingsRepository notificationSettingsRepository;
    private final UserRepository userRepository;
    private final FcmTokenRepository fcmTokenRepository;

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
    public NotificationSettingsResponse saveNotificationSettings(Long userId, NotificationSettingsRequest request) {
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

    //FCM 푸시 알림 발송
    //1. 감정 기록 알림: 매분마다 체크, 설정 시간 맞으면 발송
    //2. 주간 마감 알림: 매주 일요일 밤 11:30
    //3. 월간 마감 알림: 매월 말일 밤 11:30

    //FCM 푸시 알림 발송
    private void sendPushNotification(String fcmToken, String title, String body) {
        try {
            Message message = Message.builder()
                    .setNotification(Notification.builder()
                            .setTitle(title)
                            .setBody(body)
                            .build())
                    .setToken(fcmToken).build();
            FirebaseMessaging.getInstance().send(message);
        } catch (Exception e) {
            //발송 실패 시 로그만 남기고 계속 진행
            System.out.println("FCM 발송 실패: " + e.getMessage());
        }
    }

    //감정 기록 알림 (매분마다 체크, 설정 시간 맞으면 발송)
    @Scheduled(cron = "0 * * * * *")
    public void sendRecordNotification() {
        String currentTime = LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"));

        List<NotificationSettings> settings = notificationSettingsRepository.findByRecordNotificationTrue();

        for (NotificationSettings setting : settings) {
            User user = setting.getUser();

            //전체 알림 ON인지 확인
            if (!user.getNotificationsEnabled()) continue;

            //설정 시간과 현재 시간 비교
            if (!currentTime.equals(setting.getNotificationTime())) continue;

            fcmTokenRepository.findByUser(user).ifPresent(fcmToken ->
                    sendPushNotification(
                            fcmToken.getToken(),
                            "마음이",
                            "오늘 하루 감정을 기록해보세요💚📗"
                    )
            );
        }
    }

    //주간 마감 알림 (매주 일요일 밤 11:30)
    @Scheduled(cron = "0 30 23 * * SUN")
    public void sendWeeklyNotification() {
        List<NotificationSettings> settings = notificationSettingsRepository.findByWeeklyNotificationTrue();

        for (NotificationSettings setting : settings) {
            User user = setting.getUser();

            //전체 알림 ON인지 확인
            if (!user.getNotificationsEnabled()) continue;

            fcmTokenRepository.findByUser(user).ifPresent(fcmToken ->
                    sendPushNotification(
                            fcmToken.getToken(),
                            "마음이",
                            "이번 한 주도 고생하셨어요! 함께 이번 주 감정 기록을 마무리 해볼까요? 📊 "
                    )
            );
        }
    }

    //월간 마감 알림 (매주 말일 밤 11:30)
    @Scheduled(cron = "0 30 23 L * *")
    public void sendMonthlyNotification() {
        List<NotificationSettings> settings = notificationSettingsRepository.findByMonthlyNotificationTrue();

        for (NotificationSettings setting : settings) {
            User user = setting.getUser();

            //전체 알림 ON인지 확인
            if (!user.getNotificationsEnabled()) continue;

            fcmTokenRepository.findByUser(user).ifPresent(fcmToken ->
                    sendPushNotification(
                            fcmToken.getToken(),
                            "마음이",
                            "이번 한 달도 고생하셨어요! 함께 이번 달 감정 기록을 마무리 해보는건 어떠세요? 🌙"
                    )
            );
        }
    }
}
