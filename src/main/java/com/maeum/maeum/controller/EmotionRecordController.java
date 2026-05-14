// 감정 기록 컨트롤러
// Flutter 연동: emoji_select_screen.dart, main_screen.dart (캘린더)
// 내 이모지 선택 저장 및 조회 전용

package com.maeum.maeum.controller;

import com.maeum.maeum.dto.request.EmojiUpdateRequest;
import com.maeum.maeum.dto.response.EmotionRecordResponse;
import com.maeum.maeum.service.EmotionRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/emotions")
@RequiredArgsConstructor
public class EmotionRecordController {
    private final EmotionRecordService emotionRecordService;

    //캘린더 이모지 조회 (한  달치)
    //Flutter: main_screen.dart (캘린더 월 이동할 때마다)
    @GetMapping
    public ResponseEntity<List<EmotionRecordResponse>> getEmotionRecords(
            Authentication authentication,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        Long userId = (Long) authentication.getPrincipal();
        List<EmotionRecordResponse> response = emotionRecordService
                .getEmotionRecordsByPeriod(userId, startDate, endDate);

        return ResponseEntity.ok(response);
    }

    //내 이모지 저장
    //Flutter: emoji_select_screen.dart (이모지 선택 후 저장 버튼)
    @PostMapping("/my-emoji")
    public ResponseEntity<EmotionRecordResponse> saveMyEmoji(
            Authentication authentication,
            @RequestBody EmojiUpdateRequest request) {
        Long userId = (Long) authentication.getPrincipal();
        EmotionRecordResponse response = emotionRecordService
                .updateMyEmoji(userId, request);

        return ResponseEntity.ok(response);
    }
}
