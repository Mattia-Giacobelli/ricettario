package com.example.ricettario.service;

import com.example.ricettario.entities.*;
import com.example.ricettario.repositories.IRecipeIngredientRepository;
import com.example.ricettario.repositories.IRecipeRepository;
import com.example.ricettario.repositories.IIngredientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecipeIngredientService {

    private final IRecipeIngredientRepository recipeIngredientRepository;
    private final IRecipeRepository recipeRepository;
    private final IIngredientRepository ingredientRepository;

    public RecipeIngredientService(IRecipeIngredientRepository recipeIngredientRepository,
            IRecipeRepository recipeRepository,
            IIngredientRepository ingredientRepository) {
        this.recipeIngredientRepository = recipeIngredientRepository;
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
    }

    public boolean existsByRecipe_IdAndIngredient_Id(Integer recipeId, Integer ingredientId) {

        return recipeIngredientRepository.existsByRecipe_IdAndIngredient_Id(recipeId, ingredientId);

    }

    public void addIngredientToRecipe(int recipeId, int ingredientId, String quantity, String unit, String notes) {

        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("Recipe not found: " + recipeId));

        Ingredient ingredient = ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new RuntimeException("Ingredient not found: " + ingredientId));

        RecipeIngredient ri = new RecipeIngredient();
        ri.setRecipe(recipe);
        ri.setIngredient(ingredient);
        ri.setQuantity(quantity);
        ri.setUnit(unit);
        ri.setNotes(notes);

        if (!existsByRecipe_IdAndIngredient_Id(recipe.getId(), ingredient.getId())) {
            recipeIngredientRepository.save(ri);
        }

    }

    public List<RecipeIngredient> getIngredientsForRecipe(int recipeId) {
        return recipeIngredientRepository.findByRecipe_Id(recipeId);
    }

    public void removeIngredientFromRecipe(int recipeId, int ingredientId) {
        RecipeIngredientId id = new RecipeIngredientId(recipeId, ingredientId);
        recipeIngredientRepository.deleteById(id);
    }
}