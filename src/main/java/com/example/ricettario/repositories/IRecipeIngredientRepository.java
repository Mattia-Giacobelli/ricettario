package com.example.ricettario.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ricettario.entities.RecipeIngredient;
import com.example.ricettario.entities.RecipeIngredientId;

public interface IRecipeIngredientRepository extends JpaRepository<RecipeIngredient, RecipeIngredientId> {

    public List<RecipeIngredient> findByRecipe_Id(int recipeId);

}
