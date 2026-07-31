package com.example.ricettario.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "recipe_ratings")
public class RecipeRating {

    @Id
    @Column(name = "recipe_id")
    private Integer recipeId;

    @Column
    private Integer difficulty;

    @Column
    private Integer cost;

    @Column(name = "prep_time")
    private Integer prepTime;

    @Column(name = "taste_intensity")
    private Integer tasteIntensity;

    @Column(insertable = false, updatable = false)
    private Integer overall;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

}
