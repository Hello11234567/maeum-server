// 인증 컨트롤러
//Flutter: login_screen.dart, mypage_screen.dart, splash_screen.dart
// 카카오 로그인, 토큰 재발급, 로그아웃 API

package com.maeum.maeum.controller;

import com.maeum.maeum.dto.request.KakaoLoginRequest;
import com.maeum.maeum.dto.response.AuthResponse;
import com.maeum.maeum.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    //카카오 로그인
    //Fluter: login_screen.dart (카카오 로그인 버튼)
    @PostMapping("/kakao/login")
    public ResponseEntity<AuthResponse> kakaoLogin(@RequestBody KakaoLoginRequest request) {
        AuthResponse response = authService.kakaoLogin(request);

        return ResponseEntity.ok(response);
    }

    //Access Token 재발급
    //Flutter: splash_screen.dart (앱 시작 시 자동 호출)
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestHeader("Refresh-Token") String refrehsToken) {
        AuthResponse response = authService.refreshAccessToken(refrehsToken);

        return ResponseEntity.ok(response);
    }

    //로그아웃
    //Flutter: mypage_screen.dart (로그아웃 버튼)
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("User-Id") Long userId) {
        authService.logout(userId);

        return ResponseEntity.ok().build();
    }
}
