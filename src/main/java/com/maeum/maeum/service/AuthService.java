// 인증 서비스
// 카카오 로그인 처리
// JWT 토큰 발급
// 신규 유저 저장, 기존 유저 조회

package com.maeum.maeum.service;

import com.maeum.maeum.dto.request.KakaoLoginRequest;
import com.maeum.maeum.dto.response.AuthResponse;
import com.maeum.maeum.entity.User;
import com.maeum.maeum.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;

    //카카오 로그인 처리
    //1. 카카오 인가 코드로 카카오 액세스 토큰 발급
    //2. 카카오 액세스 토큰으로 유저 정보 조회
    //3. DB에 유저 저장 또는 조회
    //4. JWT 토큰 발급 후 반환
    public AuthResponse kakaoLogin(KakaoLoginRequest request) {
        //나중에 구현
        //1. 카카오 API 호출
        //2. 유저 정보 저장/조회
        //3. JWT 발급
        return null;
    }

    //유저 저장 또는 조회
    //카카오 ID로 기존 유저 확인, 없으면 신규 저장
    private User saveOrUpdateUser(String kakoId, String nickname, String profileImage) {
        return userRepository.findByKakaoId(kakoId)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setKakaoId(kakoId);
                    newUser.setNickname(nickname);
                    newUser.setProfileImage(profileImage);
                    newUser.setNotificationsEnabled(false);
                    return userRepository.save(newUser);
                });
    }
}
