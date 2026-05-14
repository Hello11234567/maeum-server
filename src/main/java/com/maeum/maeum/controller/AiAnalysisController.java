// AI 분석 컨트롤러
// Flutter 연동: ai_analysis_screen.dart, ai_result_screen.dart, statistics_screen.dart
// AI 분석 요청, 결과 조회 API

package com.maeum.maeum.controller;

import com.maeum.maeum.dto.request.AiAnalysisRequest;
import com.maeum.maeum.dto.response.AiAnalysisResponse;
import com.maeum.maeum.service.AiAnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("/ai-analysis")
@RequiredArgsConstructor
public class AiAnalysisController {
    private final AiAnalysisService aiAnalysisService;
    //AI 분석 요청
    //Flutter: ai_analysis_screen.dart (감정 슬라이더 입력 후 "오늘의 마음 살피러 가기" 버튼)
    @PostMapping
    public ResponseEntity<AiAnalysisResponse> analyze(
            Authentication authentication,
            @RequestBody AiAnalysisRequest request) {
        Long userId = (Long) authentication.getPrincipal();
        AiAnalysisResponse response = aiAnalysisService.analyze(userId, request);

        return ResponseEntity.ok(response);
    }

    //AI 분석 결과 조회
    //Flutter: ai_result_screen.dart, (결과 화면), main_screen.dart (캘린더 길게 누르기)
    @GetMapping
    public ResponseEntity<AiAnalysisResponse> getAnalysis(
            Authentication authentication,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        Long userId = (Long) authentication.getPrincipal();
        Optional<AiAnalysisResponse> response = aiAnalysisService.getAnalysis(userId, date);

        return response.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
