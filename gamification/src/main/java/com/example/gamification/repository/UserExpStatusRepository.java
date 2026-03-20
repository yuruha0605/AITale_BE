// DB와 대화할 수 있는 인터페이스입니다.
package com.example.gamification.repository;

import com.example.gamification.domain.UserExpStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserExpStatusRepository extends JpaRepository<UserExpStatus, Long> {

    // 사용자 식별자로 현재 상태를 찾기 위한 메서드
    Optional<UserExpStatus> findByUserId(Long userId);
}
