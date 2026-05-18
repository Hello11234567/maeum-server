// 유저 정보 응답 DTO
// 마이페이지, 프로필 수정 화면에서 유저 정보 불러올 때 반환

package com.maeum.maeum.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UserResponse {
    //유저 ID
    private Long id;

    //닉네임
    private String nickname;

    //프로필 이미지 URL
    private String profileImage;

    //나를 한 마디로
    private String intro;

    //나이대
    private String ageRange;

    //전체 알림 ON/OFF
    private Boolean notificationsEnabled;
}
