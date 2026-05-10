// 로그인 응답 DTO
// 카카오 로그인 성공 시 Flutter로 반환
// JWT 토큰과 유저 기본 정보 전달

package com.maeum.maeum.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthResponse {
    //JWT 액세스 토큰
    private String accessToken;

    //유저 ID
    private Long userId;

    //닉네임
    private String nickname;

    //프로필 이미지 URL
    private String profileImage;
}
