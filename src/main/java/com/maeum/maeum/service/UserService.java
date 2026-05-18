// 유저 서비스
// 마이페이지 유저 정보 조회
// 프로필 수정
// 회원탈퇴
// 마음이와 함께한 N일 계산

package com.maeum.maeum.service;

import com.maeum.maeum.dto.request.ProfileUpdateRequest;
import com.maeum.maeum.dto.response.UserResponse;
import com.maeum.maeum.entity.User;
import com.maeum.maeum.exception.CustomException;
import com.maeum.maeum.exception.ErrorCode;
import com.maeum.maeum.repository.UserRepository;
import com.maeum.maeum.repository.EmotionRecordRepository;
import com.maeum.maeum.repository.AiAnalysisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final EmotionRecordRepository emotionRecordRepository;
    private final AiAnalysisRepository aiAnalysisRepository;

    //유저 정보 조회 (마이페이지)
    public UserResponse getUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        return new UserResponse(
                user.getId(),
                user.getNickname(),
                user.getProfileImage(),
                user.getIntro(),
                user.getAgeRange(),
                user.getNotificationsEnabled()
        );
    }

    //마이페이지 통계 조회
    public Map<String, Object> getUserStats(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        //총 기록 일수
        long totalDays = emotionRecordRepository.countByUser(user);

        //이번 달 기록 일수
        LocalDate monthStart = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
        LocalDate monthEnd = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());
        long thisMonthDays = emotionRecordRepository.countByUserAndDateBetween(user, monthStart, monthEnd);

        //AI 분석 횟수
        long aiCount = aiAnalysisRepository.countByUser(user);

        //마음이와 함께한 N일 계산
        long daysWithMaeum = ChronoUnit.DAYS.between(
                user.getCreatedAt().toLocalDate(),
                LocalDate.now()
        ) + 1; //가입한 날부터 1일로 계산하기 위해 +1

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalDays", totalDays);
        stats.put("thisMonthDays", thisMonthDays);
        stats.put("aiCount", aiCount);
        stats.put("daysWithMaeum", daysWithMaeum);

        return stats;
    }

    //프로필 수정
    @Transactional
    public UserResponse updateProfile(Long userId, ProfileUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        user.setNickname(request.getNickname());
        user.setProfileImage(request.getProfileImage());
        user.setIntro(request.getIntro());
        user.setAgeRange(request.getAgeRange());
        return getUser(userId);
    }

    //회원탈퇴
    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        userRepository.delete(user);
    }

    //알림 전체 ON/OFF 수정
    @Transactional
    public void updateNotificationsEnabled(Long userId, Boolean enabled) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        user.setNotificationsEnabled(enabled);
        userRepository.save(user);
    }
}
