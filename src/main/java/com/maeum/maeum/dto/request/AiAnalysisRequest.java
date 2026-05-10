// AI 분석 요청 DTO
// Flutter에서 AI 분석하기 버튼 누를 때 서버로 전달
// 감정 수치 + 날짜 전달

package com.maeum.maeum.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class AiAnalysisRequest {
    //분석 날짜
    private LocalDate date;

    //기쁨 수치
    private Double joy;

    //화남 수치
    private Double anger;

    //불안 수치
    private Double anxiety;

    //평안 수치
    private Double peace;

    //슬픔 수치
    private Double sadness;
}
