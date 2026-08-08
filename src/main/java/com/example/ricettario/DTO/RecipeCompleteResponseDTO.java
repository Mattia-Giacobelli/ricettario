package com.example.ricettario.DTO;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.example.ricettario.entities.Recipe;
import com.example.ricettario.entities.RecipeIngredient;
import com.example.ricettario.entities.RecipeRating;
import com.example.ricettario.entities.Tag;

public class RecipeCompleteResponseDTO {

    private Integer id;

    private String name;

    private String description;

    private String instructions;

    private String imageUrl;

    private Integer timesPrep;

    private List<RecipeIngredient> ingredients = new ArrayList<>();

    private Set<Tag> tags = new HashSet<>();

    private RecipeRating rating;

    public RecipeCompleteResponseDTO(Recipe recipe) {

        this.id = recipe.getId();
        this.name = recipe.getName();
        this.description = recipe.getDescription();
        this.instructions = recipe.getInstructions();
        this.imageUrl = recipe.getImageUrl();
        this.timesPrep = recipe.getTimesPrep();
        this.ingredients = recipe.getIngredients();
        this.tags = recipe.getTags();
        this.rating = recipe.getRating();

    }

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

    public Integer getTimesPrep() {
        return timesPrep;
    }

    public void setTimesPrep(Integer timesPrep) {
        this.timesPrep = timesPrep;
    }

    public List<RecipeIngredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<RecipeIngredient> ingredients) {
        this.ingredients = ingredients;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public RecipeRating getRating() {
        return rating;
    }

    public void setRating(RecipeRating rating) {
        this.rating = rating;
    }

}
