// User Repository
// users 테이블 조회/저장 인터페이스
// 카카오 로그인 시 유저 조회, 신규 유저 저장에 사용

package com.maeum.maeum.repository;

import com.maeum.maeum.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    //카카오 ID로 유저 조회 (로그인 시 사용)
    Optional<User> findByKakaoId(String kakaoId);
}
