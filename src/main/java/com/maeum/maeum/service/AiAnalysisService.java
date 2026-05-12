// AI 분석 서비스
// OpenAI API 호출해서 감정 분석
// AI 분석 결과 저장/조회
// 당일 분석 결과 조회, 지난 날짜 결과 조회

package com.maeum.maeum.service;

import com.maeum.maeum.dto.request.AiAnalysisRequest;
import com.maeum.maeum.dto.response.AiAnalysisResponse;
import com.maeum.maeum.entity.AiAnalysis;
import com.maeum.maeum.entity.User;
import com.maeum.maeum.repository.AiAnalysisRepository;
import com.maeum.maeum.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AiAnalysisService {
    private final AiAnalysisRepository aiAnalysisRepository;
    private final UserRepository userRepository;
    private final EmotionRecordService emotionRecordService;

    //AI 분석 요청 및 결과 저장
    //1. 감정 수치 저장
    //2. 당일 일기 조회 (있으면 프롬프트에 반영)
    //3. OpenAI API 호출
    //4. 결과 저장
    //5. AI 이모지 캘린더에 반영
    @Transactional
    public AiAnalysisResponse analyze(Long userId, AiAnalysisRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

        //니증에 OpenAI API 연동 시 구현
        //1. 프롬프트 생성
        //2. OpenAI API 호출
        //3. 결과 파싱 (summary, careList, speechText, representativeEmoji)

        //d임시 결과 저장
        AiAnalysis analysis = aiAnalysisRepository
                .findByUserAndDate(user, request.getDate())
                .orElse(new AiAnalysis());

        analysis.setUser(user);
        analysis.setDate(request.getDate());
        //나중에 실제 AI 결과로 교체
        analysis.setSummary("임시 요약");
        analysis.setCareList("임시 케어 추천");
        analysis.setSpeechText("임시 말풍선");
        analysis.setRepresentativeEmoji("😊");

        AiAnalysis saved = aiAnalysisRepository.save(analysis);

        //AI 이모지 캘린더에 반영
        emotionRecordService.updateAiEmoji(userId, request.getDate(), saved.getRepresentativeEmoji());

        return toResponse(saved);
    }

    //특정 날짜 AI 분석 결과 조회
    //당일 결과 보기, 캘린더 날짜 길게 누를 때 사용
    public Optional<AiAnalysisResponse> getAnalysis(Long userId, LocalDate date) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));
        return aiAnalysisRepository
                .findByUserAndDate(user, date)
                .map(this::toResponse);
    }

    //AiAnalysis -> AiAnalysisResponse 변환
    private AiAnalysisResponse toResponse(AiAnalysis analysis) {
        return new AiAnalysisResponse(
                analysis.getDate(),
                analysis.getSummary(),
                analysis.getCareList(),
                analysis.getSpeechText(),
                analysis.getRepresentativeEmoji(),
                analysis.getLastModified()
        );
    }
}
