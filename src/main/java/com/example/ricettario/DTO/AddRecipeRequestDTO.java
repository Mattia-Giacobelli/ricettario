package com.example.ricettario.DTO;

import jakarta.validation.constraints.NotNull;

public class AddRecipeRequestDTO {

    @NotNull(message = "L'ID della ricetta è obbligatorio")
    private Integer recipeId;

    private String name;

    public AddRecipeRequestDTO() {
    } // Importante per la deserializzazione

    public Integer getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Integer recipeId) {
        this.recipeId = recipeId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
