// AiAnalysis Repository
// ai_analyses 테이블 조회/저장 인터페이스
// AI 분석 결과 저장/조회에 사용
// 캘린더 날짜 길게 누르면 지난 결과 보기에 사용
// 통계 AI 요약 조회에 사용

package com.maeum.maeum.repository;

import com.maeum.maeum.entity.AiAnalysis;
import com.maeum.maeum.entity.User;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AiAnalysisRepository extends JpaRepository<AiAnalysis, Long> {
    //특정 날짜의 AI 분석 결과 조회
    //당일 결과 보기, 캘린더 날짜 길게 누르면 지난 결과 보기에 사용
    Optional<AiAnalysis> findByUserAndDate(User user, LocalDate date);

    //특정 기간의 AI 분석 결과 조회 (통계 AI 요약에 사용)
    List<AiAnalysis> findByUserAndDateBetweenOrderByDateAsc(User user, LocalDate startDate, LocalDate endDate);

    //AI 분석 횟수
    long countByUser(User user);
}
