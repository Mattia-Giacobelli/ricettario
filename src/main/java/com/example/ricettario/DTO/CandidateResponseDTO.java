package com.example.ricettario.DTO;

public class CandidateResponseDTO {

    private Integer candidateId;
    private Integer recipeId;
    private String recipeName;
    private String recipeImageUrl;
    private Integer voteCount;
    private String username;

    public CandidateResponseDTO(Integer candidateId, Integer recipeId, String recipeName,
            String recipeImageUrl, Integer voteCount, String username) {
        this.candidateId = candidateId;
        this.recipeId = recipeId;
        this.recipeName = recipeName;
        this.recipeImageUrl = recipeImageUrl;
        this.voteCount = voteCount;
        this.username = username;
    }

    public Integer getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(Integer candidateId) {
        this.candidateId = candidateId;
    }

    public Integer getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Integer recipeId) {
        this.recipeId = recipeId;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getRecipeImageUrl() {
        return recipeImageUrl;
    }

    public void setRecipeImageUrl(String recipeImageUrl) {
        this.recipeImageUrl = recipeImageUrl;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
