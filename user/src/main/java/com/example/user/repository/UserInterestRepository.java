package com.example.user.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.user.domain.entity.UserInterestEntity;

@Repository
public interface UserInterestRepository extends JpaRepository<UserInterestEntity, Long> {

    public List<UserInterestEntity> findByUserSystemId(Long userSystemId);

}
