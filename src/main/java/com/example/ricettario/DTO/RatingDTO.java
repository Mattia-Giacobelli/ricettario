package com.example.ricettario.DTO;

import lombok.Data;

@Data
public class RatingDTO {

    private int difficulty = 3;
    private int cost = 3;
    private int prepTime = 3;
    private int tasteIntensity = 3;

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

}
