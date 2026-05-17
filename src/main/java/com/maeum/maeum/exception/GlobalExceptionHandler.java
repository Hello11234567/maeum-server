//전체 예외 처리 핸들러
//CustomException을 잡아서 Flutter로 통일된 형식으로 반환
//Service에서 던진 모든 예외를 여기서 처리

package com.maeum.maeum.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    //CustomException 처리
    //Service에서 throw new CustomException(ErrorCode.XXX)하면 여기서 처리
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handelCustomException(CustomException e) {
       ErrorResponse response = new ErrorResponse(false, e.getErrorCode().getMessage());

        return ResponseEntity.status(e.getErrorCode().getStatus()).body(response);
    }

    //예상치 못한 예외 처리
    //CustomException 외의 모든 예외
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(CustomException e) {
        ErrorResponse response = new ErrorResponse(false, "서버 오류가 발생했습니다.");

        return ResponseEntity.status(500).body(response);
    }
}
