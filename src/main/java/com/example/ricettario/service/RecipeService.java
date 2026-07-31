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

    private IRecipeRepository recipeRepository;

    public RecipeService(IRecipeRepository recipeRepository) {

        this.recipeRepository = recipeRepository;

    }

    public Page<Recipe> findAll(Pageable pageable) {

        return recipeRepository.findAll(pageable);

    }

}
