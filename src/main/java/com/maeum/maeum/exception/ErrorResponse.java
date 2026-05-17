//에러 응답 DTO
//GlobalExceptionHandler에서 Flutter로 반환하는 에러 응답 형식
//실패했을 때 flutter로 보내는 응답 형식 정의

package com.maeum.maeum.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {
    //성공 여부 (항상 false: 에러일 때는 사용 X,)
    private boolean success;

    //에러 메세지 (유저에게 표시)
    private String message;
}
