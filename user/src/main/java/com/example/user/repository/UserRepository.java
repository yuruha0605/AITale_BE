package com.example.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.user.domain.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    public Optional<UserEntity> findByEmailAndPassword(String email, String password);

    public Optional<UserEntity> findByEmail(String email);

}