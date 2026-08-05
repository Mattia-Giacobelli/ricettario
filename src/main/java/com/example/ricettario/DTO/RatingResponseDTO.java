package com.example.ricettario.DTO;

public class RatingResponseDTO {

    private int difficulty;
    private int cost;
    private int prepTime;
    private int tasteIntensity;
    private Float overall;

    // getters e setters
    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getPrepTime() {
        return prepTime;
    }

    public void setPrepTime(int prepTime) {
        this.prepTime = prepTime;
    }

    public int getTasteIntensity() {
        return tasteIntensity;
    }

    public void setTasteIntensity(int tasteIntensity) {
        this.tasteIntensity = tasteIntensity;
    }

    public Float getOverall() {
        return overall;
    }

    public void setOverall(Float overall) {
        this.overall = overall;
    }
}
