package com.example.ricettario.DTO;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class RecipeFormDTO {

    private Integer id;
    private String name;
    private String imageUrl;
    private String description;
    private String instructions;
    private Integer timesPrep = 0;

    private List<TagRowDTO> tags = new ArrayList<>();

    private List<IngredientRowDTO> ingredients = new ArrayList<>();

    private RatingDTO rating = new RatingDTO();

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

    public int getTimesPrep() {
        return timesPrep;
    }

    public void setTimesPrep(int timesPrep) {
        this.timesPrep = timesPrep;
    }

    public List<IngredientRowDTO> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<IngredientRowDTO> ingredients) {
        this.ingredients = ingredients;
    }

    public RatingDTO getRating() {
        return rating;
    }

    public void setRating(RatingDTO rating) {
        this.rating = rating;
    }

    public List<TagRowDTO> getTags() {
        return tags;
    }

    public void setTags(List<TagRowDTO> tags) {
        this.tags = tags;
    }

}
