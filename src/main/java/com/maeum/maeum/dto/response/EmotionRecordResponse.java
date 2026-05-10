// 감정 수치 응답 DTO
// AI 분석 화면에서 기존 수치 불러올 때 반환
// 캘린더 이모지 표시에 사용

package com.maeum.maeum.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class EmotionRecordResponse {
    //기록 날짜
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

    //내가 선택한 이모지
    private String myEmoji;

    //AI가 선정한 대표 이모지
    private String aiEmoji;

    //마지막 수정 시간
    private LocalDateTime lastModified;
}
