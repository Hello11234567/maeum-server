// 일기 응답 DTO
// 일기 화면 목록, 일기 상세 조회 시 반환

package com.maeum.maeum.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class DiaryResponse {
    //일기 ID
    private Long id;

    //일기 날짜
    private LocalDate date;

    //일기 내용
    private String content;

    //작성 시간
    private LocalDateTime createdAt;
}
