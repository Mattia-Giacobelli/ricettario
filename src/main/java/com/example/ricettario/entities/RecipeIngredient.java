package com.example.ricettario.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "recipe_ingredients")
@Data
public class RecipeIngredient {

    @EmbeddedId
    private RecipeIngredientId id = new RecipeIngredientId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("recipeId")
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("ingredientId")
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;

    @Column(name = "quantity")
    private String quantity;

    @Column(name = "unit")
    private String unit;

    @Column(name = "notes")
    private String notes;

    // getters, setters
}