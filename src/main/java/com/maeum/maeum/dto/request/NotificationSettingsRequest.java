// 알림 설정 저장 요청 DTO
// Flutter 알림 설정 화면에서 저장하기 누를 때 서버로 전달

package com.maeum.maeum.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NotificationSettingsRequest {
    //감정 기록 알림 ON/OFF
    private Boolean recordNotification;

    //알림 시간 (예: "21:00")
    private String notificationTime;

    //주간 마감 알림 ON/OFF
    private Boolean weeklyNotification;

    //월간 마감 알림 ON/OFF
    private Boolean monthlyNotification;
}
