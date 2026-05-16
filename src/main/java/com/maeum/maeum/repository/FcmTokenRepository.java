//FCM 토큰 Repository
//fcm_tokens 테이블 조회/저장
//푸시 알림 발송 시 FCM 토큰 조회에 사용

package com.maeum.maeum.repository;

import com.maeum.maeum.entity.FcmToken;
import com.maeum.maeum.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FcmTokenRepository extends JpaRepository<FcmToken, Long> {
    //유저의 FCM 토큰 조회
    Optional<FcmToken> findByUser(User user);

    //유저의 FCM 토큰 삭제 (로그아웃, 회원탈퇴 시)
    void deleteByUser(User user);
}
