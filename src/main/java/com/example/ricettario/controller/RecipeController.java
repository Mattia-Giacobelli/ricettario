package com.example.ricettario.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.ricettario.entities.Recipe;
import com.example.ricettario.service.RecipeService;

@Controller
@RequestMapping("/recipes")
public class RecipeController {

    private RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {

        this.recipeService = recipeService;

    }

    @GetMapping("")
    public String index(Model recipesM, @RequestParam(defaultValue = "0") int page) {

        Pageable pageable = PageRequest.of(page, 20, Sort.by("name").ascending());
        Page<Recipe> recipes = recipeService.findAll(pageable);

        recipesM.addAttribute("recipes", recipes);

        return "pages/recipes/index";
    }

}
