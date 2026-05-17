//에러 코드 모음
//모든 예외 상황에 대한 코드와 메세지 정의
//CustomException에서 사용
//GlobalExceptionHandler에서 HTTP 상태 코드와 메세지 반환에 사용

//ErrorCode -> CustomException -> GlobalExceptionHandler

package com.maeum.maeum.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    //유저
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."),

    //인증
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "Refresh Token을 찾을 수 없습니다."),
    KAKAO_LOGIN_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "카카오 로그인에 실패했습니다."),
    KAKAO_PROFILE_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "카카오 포르필 정보를 가져올 수 없습니다."),

    //일기
    DIARY_NOT_FOUND(HttpStatus.NOT_FOUND, "일기를 찾을 수 없습니다."),

    //감정 기록
    EMOTION_RECORD_NOT_FOUND(HttpStatus.NOT_FOUND, "감정 기록을 찾을 수 없습니다."),

    //AI 분석
    AI_ANALYSIS_NOT_FOUND(HttpStatus.NOT_FOUND, "AI 분석 결과를 찾을 수 없습니다."),
    AI_ANALYSIS_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "AI 분석에 실패했습니다."),

    //알림
    NOTIFICATION_SETTINGS_NOT_FOUND(HttpStatus.NOT_FOUND, "알림 설정을 찾을 수 없습니다.");

    private final HttpStatus status; //flutter가 내부적으로 어떻게 처리할지 결정
    private final String message; //유저한테 보여주는 텍스트

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
