//통계 컨트롤러
//Flutter 연동: statistics_screen.dart, statistics_compare_screen.dart
//주간/월간 통계 조회, AI 요약 포함

package com.maeum.maeum.controller;

import com.maeum.maeum.service.StatisticsService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
public class StatisticsController {
    private final StatisticsService statisticsService;

    //주간 통계 조회 (<> 버튼으로 날짜 이동)
    //Flutter: statistics_screen.dart (주간 탭, AI 요약 포함)
    @GetMapping("/weekly")
    public ResponseEntity<Map<String, Object>> getWeeklyStatistics(
            Authentication authentication,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        Long userId = (Long) authentication.getPrincipal();
        Map<String, Object> response = statisticsService.getWeeklyStatisticsByDate(userId, date);

        return ResponseEntity.ok(response);
    }

    //월간 통계 조회 (<> 버튼으로 날짜 이동)
    //Flutter: statistics_screen.dart (월간 탭, AI 요약 포함)
    @GetMapping("/monthly")
    public ResponseEntity<Map<String, Object>> getMonthlyStatistics(
            Authentication authentication,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        Long userId = (Long) authentication.getPrincipal();
        Map<String, Object> response = statisticsService.getMonthlyStatisticsByDate(userId, date);

        return ResponseEntity.ok(response);
    }

    //주간 비교 (이번주 vs 지난주)
    //Flutter: statistics_screen.dart (비교하기 버튼 눌렀을 때)
    @GetMapping("/weekly/compare")
    public ResponseEntity<Map<String, Object>> getWeeklyComparison(
            Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        Map<String, Object> response = statisticsService.getWeeklyComparison(userId);

        return ResponseEntity.ok(response);
    }

    //월간 비교 (이번달 vs 지난달)
    //Flutter: statistics_screen.dart (비교하기 버튼 눌렀을 때)
    @GetMapping("/monthly/compare")
    public ResponseEntity<Map<String, Object>> getMonthlyComparison(
            Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        Map<String, Object> response = statisticsService.getMonthlyComparison(userId);

        return ResponseEntity.ok(response);
    }
}
