// EmotionRecord Repository
// emotion_records 테이블 조회/저장 인터페이스
// AI 분석 화면 감정 수치 저장/조회에 사용
// 통계 화면 데이터 조회에 사용
// 캘린더 이모지 표시에 사용

package com.maeum.maeum.repository;

import com.maeum.maeum.entity.EmotionRecord;
import com.maeum.maeum.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmotionRecordRepository extends JpaRepository<EmotionRecord, Long> {
    //특정 날짜의 감정 기록 조회 (AI 분석 화면 기존 수치 불러오기)
    Optional<EmotionRecord> findByUserAndDate(User user, LocalDate date);

    //특정 기간의 감정 기록 조회 (주간/월간 통계에 사용)
    List<EmotionRecord> findByUserAndDateBetween(User user, LocalDate startDate, LocalDate endDate);

    //유저의 월별 감정 기록 조회 (캘린더에 날짜별 이모지 표시에 사용)
    List<EmotionRecord> findByUserAndDateBetweenOrderByDateAsc(User user, LocalDate startDate, LocalDate endDate);

    //총 감정 기록 일수
    long countByUser(User user);

    //기간별 감정 기록 횟수
    long countByUserAndDateBetween(User user, LocalDate startDate, LocalDate endDate);
}
