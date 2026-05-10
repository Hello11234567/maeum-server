// 일기 저장 요청 DTO
// Flutter 일기 작성 화면에서 서버로 전달할 때 사용

package com.maeum.maeum.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class DiaryRequest {
    //일기 날짜
    private LocalDate date;

    //일기 내용
    private String content;
}
