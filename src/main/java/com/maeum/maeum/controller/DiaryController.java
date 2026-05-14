//일기 컨트롤러
//Flutter 연동: diary_screen.dart
//일기 작성, 일기 조회, 일기 수정 API

package com.maeum.maeum.controller;

import com.maeum.maeum.dto.request.DiaryRequest;
import com.maeum.maeum.dto.response.DiaryResponse;
import com.maeum.maeum.service.DiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/diaries")
@RequiredArgsConstructor
public class DiaryController {
    private final DiaryService diaryService;

    //일기 작성
    //Flutter: diary_screen.dart, ai_analysis_screen.dart (일기 작성하고 올래요 -> 저장)
    @PostMapping
    public ResponseEntity<DiaryResponse> createdDiary(
            Authentication authentication, @RequestBody DiaryRequest request) {
        Long userId = (Long) authentication.getPrincipal();
        DiaryResponse response = diaryService.saveDiary(userId, request);

        return ResponseEntity.ok(response);
    }

    //특정 날짜 일기 조회
    //Flutter: diary_screen.dart (월별 리스트), ai_analysis_screen.dart (일기 있는지 확인)
    @GetMapping
    public ResponseEntity<DiaryResponse> getDiary(
            Authentication authentication,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        Long userId = (Long) authentication.getPrincipal();
        Optional<DiaryResponse> response = diaryService.getDiary(userId, date);

        return response.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //일기 목록 조회
    //Flutter: diary_screen.dart (다이어리 진입 시 목록 표시)
    @GetMapping("/list")
    public ResponseEntity<List<DiaryResponse>> getDiaryList(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        List<DiaryResponse> response = diaryService.getDiaries(userId);

        return ResponseEntity.ok(response);
    }

    //일기 수정
    //Flutter: diary_screen.dart (오늘 날짜 일기만 수정 가능)
    //@RequestBody: 수정된 일기 내용을 받음
    @PutMapping("/{diaryId")
    public ResponseEntity<DiaryResponse> updateDiary (
            Authentication authentication,
            @PathVariable Long diaryId,
            @RequestBody DiaryRequest request) {
        Long userId = (Long) authentication.getPrincipal();
        DiaryResponse response = diaryService.saveDiary(userId, request);

        return ResponseEntity.ok(response);
    }
}
