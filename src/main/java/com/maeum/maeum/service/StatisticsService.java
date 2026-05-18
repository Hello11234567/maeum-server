// 통계 서비스
// 주간/월간 감정 평균 계산
// 주간/월간 AI 요약 조회
// 지난주/지난달 비교 데이터 계산

package com.maeum.maeum.service;

import com.maeum.maeum.dto.response.EmotionRecordResponse;
import com.maeum.maeum.entity.AiAnalysis;
import com.maeum.maeum.entity.User;
import com.maeum.maeum.exception.CustomException;
import com.maeum.maeum.exception.ErrorCode;
import com.maeum.maeum.repository.AiAnalysisRepository;
import com.maeum.maeum.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatisticsService {
    private final UserRepository userRepository;
    private final EmotionRecordService emotionRecordService;
    private final AiAnalysisRepository aiAnalysisRepository;

    //이번 주 날짜 범위 계산 (월~일)
    private LocalDate getThisWeekStart() {
        return LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    }

    private LocalDate getThisWeekEnd() {
        return LocalDate.now().with((TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)));
    }

    //지난 주 날짜 범위 계산
    private LocalDate getLastWeekStart() {
        return getThisWeekStart().minusWeeks(1);
    }

    private LocalDate getLastWeekEnd() {
        return getThisWeekEnd().minusWeeks(1);
    }

    //이번 달 날짜 범위 계산
    private LocalDate getThisMonthStart() {
        return LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
    }

    private LocalDate getThisMonthEnd() {
        return LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());
    }

    //지난 달 날짜 범위 계산
    private LocalDate getLastMonthStart() {
        return getThisMonthStart().minusMonths(1);
    }

    private LocalDate getLastMonthEnd() {
        return getThisMonthEnd().minusMonths(1);
    }

    //감정 평균 계산
    //기록한 날만 포함해서 평균 계산
    private Map<String, Double> calculateAverage(List<EmotionRecordResponse> records) {
        Map<String, Double> avg = new HashMap<>();
        if (records.isEmpty()) {
            avg.put("joy", 0.0);
            avg.put("anger", 0.0);
            avg.put("anxiety", 0.0);
            avg.put("peace", 0.0);
            avg.put("sadness", 0.0);
            return avg;
        }
        avg.put("joy", records.stream().mapToDouble(EmotionRecordResponse::getJoy).average().orElse(0));
        avg.put("anger", records.stream().mapToDouble(EmotionRecordResponse::getAnger).average().orElse(0));
        avg.put("anxiety", records.stream().mapToDouble(EmotionRecordResponse::getAnxiety).average().orElse(0));
        avg.put("peace", records.stream().mapToDouble(EmotionRecordResponse::getPeace).average().orElse(0));
        avg.put("sadness", records.stream().mapToDouble(EmotionRecordResponse::getSadness).average().orElse(0));
        return avg;
    }

    //특정 주 통계 조회 (< > 버튼으로 이전/다음 주 볼 때)
    public Map<String, Object> getWeeklyStatisticsByDate(Long userId, LocalDate date) {
        LocalDate weekStart = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate weekEnd = date.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        List<EmotionRecordResponse> records = emotionRecordService
                .getEmotionRecordsByPeriod(userId, weekStart, weekEnd);

        Map<String, Object> result = new HashMap<>();
        result.put("average", calculateAverage(records));
        result.put("records", records);
        result.put("weekStart", weekStart);
        result.put("weekEnd", weekEnd);

        //AI 요약 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        List<AiAnalysis> analyses = aiAnalysisRepository
                .findByUserAndDateBetweenOrderByDateAsc(user, weekStart, weekEnd);
        if (!analyses.isEmpty()) {
            result.put("aiSummary", analyses.getLast().getSummary());
        }

        return result;
    }

    //특정 달 통계 조회 (< > 버튼으로 이전/다음 달 볼 때)
    public Map<String, Object> getMonthlyStatisticsByDate(Long userId, LocalDate date) {
        LocalDate monthStart = date.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate monthEnd = date.with(TemporalAdjusters.lastDayOfMonth());

        List<EmotionRecordResponse> records = emotionRecordService
                .getEmotionRecordsByPeriod(userId, monthStart, monthEnd);

        Map<String, Object> result = new HashMap<>();
        result.put("average", calculateAverage(records));
        result.put("records", records);
        result.put("monthStart", monthStart);
        result.put("monthEnd", monthEnd);

        //AI 요약 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        List<AiAnalysis> analyses = aiAnalysisRepository
                .findByUserAndDateBetweenOrderByDateAsc(user, monthStart, monthEnd);
        if (!analyses.isEmpty()) {
            result.put("aiSummary", analyses.getLast().getSummary());
        }

        return result;
    }

    //주간 비교 (이번 주 vs 지난 주)
    public Map<String, Object> getWeeklyComparison(Long userId) {
        Map<String, Object> result = new HashMap<>();
        result.put("current", calculateAverage(emotionRecordService
                .getEmotionRecordsByPeriod(userId, getThisWeekStart(), getThisWeekEnd())));
        result.put("previous", calculateAverage(emotionRecordService
                .getEmotionRecordsByPeriod(userId, getLastWeekStart(), getLastWeekEnd())));

        //AI 요약 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        List<AiAnalysis> analyses = aiAnalysisRepository
                .findByUserAndDateBetweenOrderByDateAsc(user, getThisWeekStart(), getThisWeekEnd());
        if (!analyses.isEmpty()) {
            result.put("aiSummary", analyses.getLast().getSummary());
        }

        return result;
    }

    //주간 비교 (이번 달 vs 지난 달)
    public Map<String, Object> getMonthlyComparison(Long userId) {
        Map<String, Object> result = new HashMap<>();
        result.put("current", calculateAverage(emotionRecordService
                .getEmotionRecordsByPeriod(userId, getThisMonthStart(), getThisMonthEnd())));
        result.put("previous", calculateAverage(emotionRecordService
                .getEmotionRecordsByPeriod(userId, getLastMonthStart(), getLastMonthEnd())));

        //AI 요약 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        List<AiAnalysis> analyses = aiAnalysisRepository
                .findByUserAndDateBetweenOrderByDateAsc(user, getThisMonthStart(), getThisMonthEnd());
        if (!analyses.isEmpty()) {
            result.put("aiSummary", analyses.getLast().getSummary());
        }

        return result;
    }
}
