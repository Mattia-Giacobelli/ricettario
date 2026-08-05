package com.example.ricettario.DTO;

import java.util.List;

public class RecipeResponseDTO {

    private Integer id;
    private String name;
    private String description;
    private String instructions;
    private String imageUrl;
    private List<String> tags;
    private List<IngredientResponseDTO> ingredients;
    private RatingResponseDTO rating;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<IngredientResponseDTO> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<IngredientResponseDTO> ingredients) {
        this.ingredients = ingredients;
    }

    public RatingResponseDTO getRating() {
        return rating;
    }

    public void setRating(RatingResponseDTO rating) {
        this.rating = rating;
    }
}
