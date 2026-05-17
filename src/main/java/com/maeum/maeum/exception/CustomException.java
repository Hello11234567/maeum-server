//커스텀 예외 클래스
//Service에서 예외 발생 시 사용
//throw new CustomException(ErrorCode.USER_NOT_FOUND) 형식으로 사용
//GlobalExceptionHandler에서 잡아서 처리

package com.maeum.maeum.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());

        this.errorCode = errorCode;
    }
}
