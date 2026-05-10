// 카카오 로그인 요청 DTO
// Flutter에서 카카오 인가 코드를 서버로 전달할 때 사용
// 카카오 로그인 후 받은 인가 코드를 담아서 서버로 전송

package com.maeum.maeum.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoLoginRequest {
    //카카오 로그인 후 받은 인가 코드
    private String code;
}
