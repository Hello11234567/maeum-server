// 알림 설정 응답 DTO
// 알림 설정 화면 진입 시 기존 설정값 반환

package com.maeum.maeum.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NotificationSettingsResponse {
    //감정 기록 알림 ON/OFF
    private Boolean recordNotification;

    //알림 시간
    private String notificationTime;

    //주간 마감 알림 ON/OFF
    private Boolean weeklyNotification;

    //월간 마감 알림 ON/OFF
    private Boolean monthlyNotification;
}
