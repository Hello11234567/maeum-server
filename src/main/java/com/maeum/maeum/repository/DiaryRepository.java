// Diary Repository
// diaries 테이블 조회/저장 인터페이스
// 일기 화면 목록 조회, 일기 저장/수정에 사용
// AI 분석 시 당일 일기 조회에 사용

package com.maeum.maeum.repository;

import com.maeum.maeum.entity.Diary;
import com.maeum.maeum.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DiaryRepository {
    //유저의 모든 일기 조회 (일기 화면 목록)
    List<Diary> findByUserOrderByDateDesc(User user);

    //특정 날짜의 일기 조회 (AI 분석 시 당일 일기 확인)
    Optional<Diary> findByUserAndDate(User user, LocalDate date);
}
