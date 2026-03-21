package com.example.learning.domain.entity;

public enum Difficulty {
    LOW(1),
    MEDIUM(2),
    HIGH(3);

    private final int score;

    Difficulty(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }
}
