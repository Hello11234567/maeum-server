// 이모지 업데이트 요청 DTO
// 캘린더에서 날짜 클릭해서 이모지 선택할 때 서버로 전달

package com.maeum.maeum.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class EmojiUpdateRequest {

    // 선택한 날짜
    private LocalDate date;

    // 선택한 이모지
    private String myEmoji;
}
