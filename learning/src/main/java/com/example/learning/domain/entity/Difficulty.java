package com.example.learning.domain.entity;

public enum Difficulty {
    EASY(1),
    NORMAL(2),
    HARD(3);

    private final int score;

    Difficulty(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }
}
