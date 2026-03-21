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
@Table(name = "user_interest_relationship")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInterestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userInterestId; // 비즈니스적 의미는 없지만, 관리를 위한 '의도적인 PK'

    @Column(name = "user_system_id")
    private Long userSystemId;

    @Column(name = "interest_id")
    private Long interestId;
}