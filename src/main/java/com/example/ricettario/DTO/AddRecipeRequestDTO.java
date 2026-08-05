package com.example.ricettario.DTO;

import jakarta.validation.constraints.NotNull;

public class AddRecipeRequestDTO {

    @NotNull(message = "L'ID della ricetta è obbligatorio")
    Integer recipeId;

    public AddRecipeRequestDTO() {
    } // Importante per la deserializzazione

    public Integer getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Integer recipeId) {
        this.recipeId = recipeId;
    }

}
