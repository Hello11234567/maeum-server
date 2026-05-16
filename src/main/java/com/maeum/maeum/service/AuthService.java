// 인증 서비스
// 카카오 로그인 처리
// JWT 토큰 발급
// 신규 유저 저장, 기존 유저 조회

package com.maeum.maeum.service;

import com.maeum.maeum.config.JwtUtil;
import com.maeum.maeum.dto.request.KakaoLoginRequest;
import com.maeum.maeum.dto.response.AuthResponse;
import com.maeum.maeum.entity.RefreshToken;
import com.maeum.maeum.entity.User;
import com.maeum.maeum.repository.RefreshTokenRepository;
import com.maeum.maeum.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;
    private final RestTemplate restTemplate;

    //카카오 로그인 처리
    //1. 카카오 인가 코드로 카카오 액세스 토큰 발급
    //2. 카카오 액세스 토큰으로 유저 정보 조회
    //3. DB에 유저 저장 또는 조회
    //4. JWT 토큰 발급 후 반환
    @Transactional
    public AuthResponse kakaoLogin(KakaoLoginRequest request) {

        //1. 카카오 API 호출
        //2. 유저 정보 저장/조회
        //3. JWT 발급
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer" + request.getAccessToken());

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.GET,
                entity,
                (Class<Map<String, Object>>) (Class<?>) Map.class
        );

        Map<String, Object> kakaoUser = response.getBody();
        if (kakaoUser == null) throw new RuntimeException("카카오 유저 정보를 가져올 수 없습니다.");
        String kakaoId = String.valueOf(kakaoUser.get("id"));

        Map<String, Object> properties = (Map<String, Object>) kakaoUser.get("properties");
        if (properties == null) throw new RuntimeException("카카오 프로필 정보를 가져올 수 없습니다.");
        String nickname = (String) properties.get("nickname");
        String profileImage = (String) properties.get("profile_image");

        //유저 저장 또는 조회
        User user = saveOrUpdateUser(kakaoId, nickname, profileImage);

        //Access Token 생성
        String accessToken = jwtUtil.generateAccessToken(user.getId());

        //Refresh Token 생성
        String refreshTokenString = jwtUtil.generateRefreshToken(user.getId());

        //Refresh Token DB 저장
        RefreshToken refreshToken = refreshTokenRepository
                .findByUser(user)
                .orElse(new RefreshToken());

        refreshToken.setUser(user);
        refreshToken.setToken(refreshTokenString);
        refreshToken.setExpiryDate(LocalDateTime.now().plusDays(30));
        refreshTokenRepository.save(refreshToken);

        return new AuthResponse(
                accessToken,
                refreshTokenString,
                user.getId(),
                user.getNickname(),
                user.getProfileImage()
        );
    }

    //Refresh Token으로 Access Token 재발급
    @Transactional
    public AuthResponse refreshAccessToken(String refreshToken) {
        //Refresh Token 검증
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new RuntimeException("유효하지 않은 Refresh Token입니다.");
        }

        //DB에서 Refresh Token 조회
        RefreshToken storedToken = refreshTokenRepository
                .findByToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Refresh Token을 찾을 수 없습니다."));

        //만료 확인
        if (storedToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(storedToken);
            throw new RuntimeException("만료된 Refresh Token입니다.");
        }

        User user = storedToken.getUser();

        //새로운 Access Token 생성
        String newAccessToken = jwtUtil.generateAccessToken(user.getId());

        //Refresh Token 갱신 (30일 연장)
        storedToken.setExpiryDate(LocalDateTime.now().plusDays(30));
        refreshTokenRepository.save(storedToken);

        return new AuthResponse(
                newAccessToken,
                refreshToken,
                user.getId(),
                user.getNickname(),
                user.getProfileImage()
        );
    }

    //로그아웃
    @Transactional
    public void logout(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));
        refreshTokenRepository.deleteByUser(user);
    }

    //유저 저장 또는 조회
    //카카오 ID로 기존 유저 확인, 없으면 신규 저장
    private User saveOrUpdateUser(String kakaoId, String nickname, String profileImage) {
        return userRepository.findByKakaoId(kakaoId)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setKakaoId(kakaoId);
                    newUser.setNickname(nickname);
                    newUser.setProfileImage(profileImage);
                    newUser.setNotificationsEnabled(false);
                    return userRepository.save(newUser);
                });
    }
}
