// RefreshToken Repository
// refresh_tokens 테이블 조회/저장 인터페이스
// Refresh Token 검증 및 갱신에 사용

package com.maeum.maeum.repository;

import com.maeum.maeum.entity.RefreshToken;
import com.maeum.maeum.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    //Token 문자열로 조회 (Access Token 재발급 시 사용)
    Optional<RefreshToken> findByToken(String token);

    //유저로 조회 (로그아웃 시 삭제용)
    Optional<RefreshToken> findByUser(User user);

    //유저의 Refresh Token 삭제 (로그아웃 시 사용)
    void deleteByUser(User user);
}
