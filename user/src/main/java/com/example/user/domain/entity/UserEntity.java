package com.example.user.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_tbl")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_system_id")
    private Long userSystemId;

    @Column(nullable = false, unique = true)
    private String email;

    private String password;

    private int age;

    @Column(name = "current_level", nullable = false)
    private int currentLevel = 1; // 기본값 1

    @Column(name = "assigned_difficulty")
    private String assignedDifficulty; // 학습 난이도

    public void assignDifficulty(String assignedDifficulty) {
        this.assignedDifficulty = assignedDifficulty;
    }
    
    public int increaseLevel() {
        this.currentLevel++;
        return this.currentLevel;
    }

    public void changeLevel(int level) {
        this.currentLevel = level;
    }
}
