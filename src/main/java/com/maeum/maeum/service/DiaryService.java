// 일기 서비스
// 일기 저장, 수정, 조회
// AI 분석 시 당일 일기 조회에 사용

package com.maeum.maeum.service;

import com.maeum.maeum.dto.request.DiaryRequest;
import com.maeum.maeum.dto.response.DiaryResponse;
import com.maeum.maeum.entity.Diary;
import com.maeum.maeum.entity.User;
import com.maeum.maeum.repository.DiaryRepository;
import com.maeum.maeum.repository.UserRepository;
import com.maeum.maeum.exception.CustomException;
import com.maeum.maeum.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiaryService {
    private final DiaryRepository diaryRepository;
    private final UserRepository userRepository;

    //일기 저장 (일기 작성 화면에서 저장하기 누를 때)
    @Transactional
    public DiaryResponse saveDiary(Long userId, DiaryRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        //당일 일기가 이미 있으면 수정, 없으면 새로 저장
        Diary diary = diaryRepository.findByUserAndDate(user, request.getDate())
                .orElse(new Diary());

        diary.setUser(user);
        diary.setDate(request.getDate());
        diary.setContent(request.getContent());

        Diary saved = diaryRepository.save(diary);
        return new DiaryResponse(
                saved.getId(),
                saved.getDate(),
                saved.getContent(),
                saved.getCreatedAt()
        );
    }

    //특정 날짜 일기 조회 (Controller용)
    public Optional<DiaryResponse> getDiary(Long userId, LocalDate date) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return diaryRepository.findByUserAndDate(user, date)
                .map(diary -> new DiaryResponse(
                        diary.getId(),
                        diary.getDate(),
                        diary.getContent(),
                        diary.getCreatedAt()
                ));
    }

    //일기 목록 조회 (일기 화면 리스트)
    public List<DiaryResponse> getDiaries(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return diaryRepository.findByUserOrderByDateDesc(user)
                .stream()
                .map(diary -> new DiaryResponse(
                        diary.getId(),
                        diary.getDate(),
                        diary.getContent(),
                        diary.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    //당일 일기 조회 (AI 분석 시 일기 여부 확인)
    public Optional<Diary> getTodayDiary(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        return diaryRepository.findByUserAndDate(user, LocalDate.now());
    }
}
