// 유저 컨트롤러
//Flutter: mypage_screen.dart, profile_edit_screen.dart
// 유저 정보 조회, 프로필 수정, 회원탈퇴 API

package com.maeum.maeum.controller;

import com.maeum.maeum.dto.request.ProfileUpdateRequest;
import com.maeum.maeum.dto.response.UserResponse;
import com.maeum.maeum.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    //내 정보 조회
    //Flutter: mypage_screen.dart (마이페이지 진입 시)
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMyProfile(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        UserResponse response = userService.getUser(userId);

        return ResponseEntity.ok(response);
    }

    //프로필 수정
    //Flutter: proflie_edit_screen.dart (프로필 편집 화면에서 저장 버튼)
    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateProfile(
            Authentication authentication,
            @RequestBody ProfileUpdateRequest request) {
        Long userId = (Long) authentication.getPrincipal();
        UserResponse response = userService.updateProfile(userId, request);

        return ResponseEntity.ok(response);
    }

    //회원탈퇴
    //Flutter: profile_edit_screen.dart (프로필 편집 화면에서 회원 탈퇴 버튼)
    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteAccount(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        userService.deleteUser(userId);

        return ResponseEntity.ok().build();
    }

    //전체 알림 설정 ON/OFF
    //Flutter: notification_settings_screen.dart (전체 알림 스위치)
    @PutMapping("/me/notifications")
    public ResponseEntity<Void> updateNotificationsEnabled(
            Authentication authentication,
            @RequestParam Boolean enabled) {
        Long userId = (Long) authentication.getPrincipal();
        userService.updateNotificationsEnabled(userId, enabled);

        return ResponseEntity.ok().build();
    }
}
