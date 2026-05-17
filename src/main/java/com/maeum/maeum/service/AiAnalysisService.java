// AI 분석 서비스
// OpenAI API 호출해서 감정 분석
// AI 분석 결과 저장/조회
// 당일 분석 결과 조회, 지난 날짜 결과 조회

package com.maeum.maeum.service;

import com.maeum.maeum.dto.request.AiAnalysisRequest;
import com.maeum.maeum.dto.response.AiAnalysisResponse;
import com.maeum.maeum.entity.AiAnalysis;
import com.maeum.maeum.entity.User;
import com.maeum.maeum.exception.CustomException;
import com.maeum.maeum.exception.ErrorCode;
import com.maeum.maeum.repository.AiAnalysisRepository;
import com.maeum.maeum.repository.UserRepository;
import com.maeum.maeum.repository.DiaryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Optional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AiAnalysisService {
    private final AiAnalysisRepository aiAnalysisRepository;
    private final UserRepository userRepository;
    private final DiaryRepository diaryRepository;
    private final EmotionRecordService emotionRecordService;
    private final RestTemplate restTemplate;

    @Value("${openai.api-key}")
    private String openAiApiKey;

    @Value("${openai.model}")
    private String openAiModel;

    //AI 분석 요청 및 결과 저장
    //1. 감정 수치 저장
    //2. 당일 일기 조회 (있으면 프롬프트에 반영)
    //3. OpenAI API 호출
    //4. 결과 저장
    //5. AI 이모지 캘린더에 반영
    @Transactional
    public AiAnalysisResponse analyze(Long userId, AiAnalysisRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        //니증에 OpenAI API 연동 시 구현
        //1. 프롬프트 생성
        //2. OpenAI API 호출
        //3. 결과 파싱 (summary, careList, speechText, representativeEmoji)

        //1. 당일 일기 조회
        String diaryContent = diaryRepository
                .findByUserAndDate(user, request.getDate())
                .map(diary -> diary.getContent())
                .orElse("");

        //2. 프롬프트 생성
        String prompt = buildPrompt(request, diaryContent, user);

        //3. OpenAI API 호출
        Map<String, Object> aiResult = callOpenAi(prompt);

        //4. 결과 저장
        AiAnalysis analysis = aiAnalysisRepository
                .findByUserAndDate(user, request.getDate())
                .orElse(new AiAnalysis());

        analysis.setUser(user);
        analysis.setDate(request.getDate());
        analysis.setSummary((String) aiResult.get("summary"));
        analysis.setCareList((String) aiResult.get("careList"));
        analysis.setSpeechText((String) aiResult.get("speechText"));
        analysis.setRepresentativeEmoji((String) aiResult.get("emoji"));

        AiAnalysis saved = aiAnalysisRepository.save(analysis);

        //AI 이모지 캘린더에 반영
        emotionRecordService.updateAiEmoji(userId, request.getDate(), saved.getRepresentativeEmoji());

        return toResponse(saved);
    }

    //특정 날짜 AI 분석 결과 조회
    //당일 결과 보기, 캘린더 날짜 길게 누를 때 사용
    public Optional<AiAnalysisResponse> getAnalysis(Long userId, LocalDate date) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
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

    //프롬프트 생성
    private String buildPrompt(AiAnalysisRequest request, String diaryContent, User user) {
        return String.format("""
                사용자 정보: %s, %s
                오늘의 감정 수치 (0~10):
                기쁨: %.1f, 화남: %.1f, 불안:%.1f, 평안:%.1f 슬픔:%.1f
                오늘의 일기: %s
                
                위 정보를 바탕으로 다음 형식으로 응답해줘 (Json):
                {
                    "summary": "하루 감정 요약 (2~3문장)",
                    "careList": "감정 케어 추천 3가지 (줄바꿈으로 구분)",
                    "speechText": "따뜻한 한 마디 (1~2문장)",
                    "emoji": "대표 이모지 1개"
                }
                JSON만 반환하고 다른 텍스트는 포함하지 마세요.
                """,
                user.getNickname(),
                user.getIntro() != null ? user.getIntro() : "",
                request.getJoy(), request.getAnger(), request.getAnxiety(), request.getPeace(), request.getSadness(),
                diaryContent.isEmpty() ? "작성하지 않음" : diaryContent
        );
    }

    //OpenAI API 호출
    @SuppressWarnings("unchecked")
    private Map<String, Object> callOpenAi(String prompt) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer" + openAiApiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();
        body.put("model", openAiModel);
        body.put("messages", List.of(Map.of("role", "User", "content", prompt)));
        body.put("max_tokens", 1000);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                "https://api.openai.com/v1/chat/completions",
                HttpMethod.POST,
                entity,
                Map.class
        );

        Map<String, Object> responseBody = response.getBody();
        List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
        Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
        String content = (String) message.get("content");

        //JSON 파일
        try {
            content = content.trim();
            if (content.startsWith("```json")) content = content.substring(7);
            if (content.startsWith("```")) content = content.substring(3);
            if (content.endsWith("```")) content = content.substring(0, content.length() - 3);

            ObjectMapper mapper = new ObjectMapper();

            return mapper.readValue(content.trim(), Map.class);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.AI_ANALYSIS_FAILED);
        }
    }
}
