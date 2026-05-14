//알림 컨트롤러
//Flutter 연동: notification_settings_screen.dart
//알림 설정 조회, 저장 API

package com.maeum.maeum.controller;

import com.maeum.maeum.dto.request.NotificationSettingsRequest;
import com.maeum.maeum.dto.response.NotificationSettingsResponse;
import com.maeum.maeum.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    //알림 설정 조회
    //Flutter: notification_settings_screen.dart (알림설정 화면 진입시)
    @GetMapping("/settings")
    public ResponseEntity<NotificationSettingsResponse> getNotificationSettings(
            Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        NotificationSettingsResponse response = notificationService.getNotificationSettings(userId);

        return ResponseEntity.ok(response);
    }

    //알림 설정 저장
    //Flutter: notification_settings_screen.dart (저장 버튼)
    @PutMapping("/settings")
    public ResponseEntity<NotificationSettingsResponse> saveNotificationSettings(
            Authentication authentication,
            @RequestBody NotificationSettingsRequest request) {
        Long userId = (Long) authentication.getPrincipal();
        NotificationSettingsResponse response = notificationService.saveNotificationSetings(userId, request);

        return ResponseEntity.ok(response);
    }
}
