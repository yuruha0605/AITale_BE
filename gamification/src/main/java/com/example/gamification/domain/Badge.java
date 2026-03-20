// 획득 가능한 배지의 마스터 정보를 담는 엔티티입니다.
package com.example.gamification.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Badge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String type; // 예: BOOKWORM, FIRST_STEP
    private String imageUrl;

    @Builder
    public Badge(String name, String type, String imageUrl) {
        this.name = name;
        this.type = type;
        this.imageUrl = imageUrl;
    }
}
