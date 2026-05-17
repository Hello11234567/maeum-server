// 감정 수치 서비스
// 감정 수치 저장/조회
// 캘린더 이모지 표시에 사용
// 통계 데이터 조회에 사용

package com.maeum.maeum.service;

import com.maeum.maeum.dto.request.EmotionRecordRequest;
import com.maeum.maeum.dto.response.EmotionRecordResponse;
import com.maeum.maeum.dto.request.EmojiUpdateRequest;
import com.maeum.maeum.entity.EmotionRecord;
import com.maeum.maeum.entity.User;
import com.maeum.maeum.exception.CustomException;
import com.maeum.maeum.exception.ErrorCode;
import com.maeum.maeum.repository.EmotionRecordRepository;
import com.maeum.maeum.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmotionRecordService {
    private final EmotionRecordRepository emotionRecordRepository;
    private final UserRepository userRepository;

    //감정 수치 저장 (AI 분석하기 버튼 누를 때)
    //당일 기록 있으면 수정, 없으면 새로 저장
    @Transactional
    public EmotionRecordResponse saveEmotionRecord(Long userId, EmotionRecordRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        EmotionRecord record = emotionRecordRepository
                .findByUserAndDate(user, request.getDate())
                .orElse(new EmotionRecord());

        record.setUser(user);
        record.setDate(request.getDate());
        record.setJoy(request.getJoy());
        record.setAnger(request.getAnger());
        record.setAnxiety(request.getAnxiety());
        record.setPeace(request.getPeace());
        record.setSadness(request.getSadness());

        EmotionRecord saved = emotionRecordRepository.save(record); //db에 감정 수치 저장, 저장된 결과를 saved 변수에 담음
        return toResponse(saved); //entity -> dto로 변환
    }

    //당일 감정 수치 조회 (AI 분석 화면 기존 수치 불러오기, AI 분석 화면 들어갈 때)
    public Optional<EmotionRecordResponse> getTodayEmotionRecord(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return emotionRecordRepository
                .findByUserAndDate(user, LocalDate.now())
                .map(this::toResponse);
    }

    //기간 별 감정 기록 조회 (주간/월간 통계 화면에 사용)
    public List<EmotionRecordResponse> getEmotionRecordsByPeriod(Long userId, LocalDate startDate, LocalDate endDate) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return emotionRecordRepository
                .findByUserAndDateBetween(user, startDate, endDate)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    //AI 이모지 업데이트 (AI 분석 완료 후 캘린더에 표시)
    @Transactional
    public void updateAiEmoji(Long userId, LocalDate date, String aiEmoji) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        emotionRecordRepository.findByUserAndDate(user, date)
                .ifPresent(record -> {
                    record.setAiEmoji(aiEmoji);
                    emotionRecordRepository.save(record);
                });
    }

    //내 이모지 업데이트 (캘린더에서 날짜 클릭해서 이모지 선택할 때)
    @Transactional
    public EmotionRecordResponse updateMyEmoji(Long userId, EmojiUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        EmotionRecord record = emotionRecordRepository
                .findByUserAndDate(user, request.getDate())
                .orElse(new EmotionRecord());
        record.setUser(user);
        record.setDate(request.getDate());
        record.setMyEmoji(request.getMyEmoji());
        EmotionRecord saved = emotionRecordRepository.save(record);

        return toResponse(saved);
    }

    //EmotionRecord -> EmotionRecordResponse 변환
    private EmotionRecordResponse toResponse(EmotionRecord record) {
        return new EmotionRecordResponse(
                record.getDate(),
                record.getJoy(),
                record.getAnger(),
                record.getAnxiety(),
                record.getPeace(),
                record.getSadness(),
                record.getMyEmoji(),
                record.getAiEmoji(),
                record.getLastModified()
        );
    }
}
