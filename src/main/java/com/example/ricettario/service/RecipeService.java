package com.example.ricettario.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.ricettario.entities.Recipe;
import com.example.ricettario.repositories.IRecipeRepository;

@Service
@Transactional(readOnly = true)
public class RecipeService {

    private final IRecipeRepository recipeRepository;

    public RecipeService(IRecipeRepository recipeRepository) {

        this.recipeRepository = recipeRepository;

    }

    public Page<Recipe> findAll(Pageable pageable) {

        return recipeRepository.findAll(pageable);

    }

    public Recipe findById(int id) {

        return recipeRepository.findById(id).orElseThrow();

    }

    @Transactional
    public Recipe create(Recipe recipe) {

        return recipeRepository.save(recipe);

    }

    @Transactional(rollbackFor = Exception.class)
    public Recipe update(Recipe recipe) {

        return recipeRepository.save(recipe);

    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(int id) {

        recipeRepository.deleteById(id);

    }

}
