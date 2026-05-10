// AI 분석 결과 응답 DTO
// AI 분석 완료 후 Flutter로 결과 반환
// AI 분석 결과 화면에 표시

package com.maeum.maeum.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class AiAnalysisResponse {
    //분석 날짜
    private LocalDate date;

    //AI 하루 요약
    private String summary;

    //AI 감정 케어 추천 목록 (JSON)
    private String careList;

    //캐릭터 말풍선 텍스트
    private String speechText;

    //AI 대표 이모지 (캘린더에 표시)
    private String representativeEmoji;

    //마지막 수정 시간
    private LocalDateTime lastModified;
}
