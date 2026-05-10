// 프로필 수정 요청 DTO
// Flutter 프로필 수정 화면에서 서버로 전달할 때 사용
// 닉네임, 프로필 이미지, 나를 한 마디로, 나이대 수정

package com.maeum.maeum.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProfileUpdateRequest {
    //수정할 닉네임
    private String nickname;

    //수정할 프로필 이미지 URL
    private String profileImage;

    //나를 한 마디로 (AI 분석 프롬프트에 반영)
    private String intro;

    //나이대 (AI 분석 프롬프트에 반영)
    private String ageRange;
}
