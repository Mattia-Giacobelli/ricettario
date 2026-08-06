package com.example.ricettario.DTO;

import java.util.List;

public class RecipeSuggestionRequestDTO {

    private List<String> preferredTags;
    private String description;
    private String difficulty;
    private Integer maxPrepTime;

    // getters e setters
    public List<String> getPreferredTags() {
        return preferredTags;
    }

    public void setPreferredTags(List<String> preferredTags) {
        this.preferredTags = preferredTags;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public Integer getMaxPrepTime() {
        return maxPrepTime;
    }

    public void setMaxPrepTime(Integer maxPrepTime) {
        this.maxPrepTime = maxPrepTime;
    }
}
