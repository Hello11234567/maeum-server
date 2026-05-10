// 감정 수치 저장 요청 DTO
// Flutter AI 분석 화면에서 감정 수치 입력 후 서버로 전달할 때 사용
// 날짜별 5가지 감정 수치 저장

package com.maeum.maeum.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class EmotionRecordRequest {
    //기록 날짜
    private LocalDate date;

    //기쁨 수치 (0-10)
    private Double joy;

    //화남 수치 (0-10)
    private Double anger;

    //불안 수치 (0-10)
    private Double anxiety;

    //편안 수치 (0-10)
    private Double peace;

    //슬픔 수치 (0-10)
    private Double sadness;
}
