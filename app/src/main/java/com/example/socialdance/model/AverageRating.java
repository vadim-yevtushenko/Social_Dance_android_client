package com.example.socialdance.model;

public class AverageRating {
    private float averageRating;
    private int ratingCount;

    public AverageRating(float averageRating, int ratingCount) {
        this.averageRating = averageRating;
        this.ratingCount = ratingCount;
    }

    public float getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(float averageRating) {
        this.averageRating = averageRating;
    }

    public int getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(int ratingCount) {
        this.ratingCount = ratingCount;
    }
}
