package com.example.ricettario.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.ricettario.entities.RecipeRating;
import com.example.ricettario.repositories.IRecipeRatingRepository;

@Service
@Transactional(readOnly = true)
public class RecipeRatingService {

    private final IRecipeRatingRepository recipeRatingRepository;

    public RecipeRatingService(IRecipeRatingRepository recipeRatingRepository) {

        this.recipeRatingRepository = recipeRatingRepository;

    }

    public RecipeRating findByRecipeId(Integer recipeId) {
        return recipeRatingRepository.findById(recipeId).orElse(null);
    }

    @Transactional
    public RecipeRating create(RecipeRating recipeRating) {

        return recipeRatingRepository.save(recipeRating);

    }

    @Transactional
    public RecipeRating update(RecipeRating recipeRating) {

        return recipeRatingRepository.save(recipeRating);

    }

}
