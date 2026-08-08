package com.example.ricettario.controller.api;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import com.example.ricettario.DTO.IngredientResponseDTO;
import com.example.ricettario.DTO.RatingResponseDTO;
import com.example.ricettario.DTO.RecipeResponseDTO;
import com.example.ricettario.entities.Recipe;
import com.example.ricettario.entities.RecipeRating;
import com.example.ricettario.entities.Tag;
import com.example.ricettario.service.IngredientService;
import com.example.ricettario.service.RecipeIngredientService;
import com.example.ricettario.service.RecipeRatingService;
import com.example.ricettario.service.RecipeService;
import com.example.ricettario.service.TagService;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api/recipes")
public class RecipeAPIController {

    private final RecipeService recipeService;
    private final TagService tagService;
    private final IngredientService ingredientService;
    private final RecipeRatingService recipeRatingService;
    private final RecipeIngredientService recipeIngredientService;

    @Value("${app.upload.dir}")
    private String uploadDir;

    public RecipeAPIController(RecipeService recipeService, TagService tagService, IngredientService ingredientService,
            RecipeRatingService recipeRatingService, RecipeIngredientService recipeIngredientService) {

        this.recipeService = recipeService;
        this.tagService = tagService;
        this.ingredientService = ingredientService;
        this.recipeRatingService = recipeRatingService;
        this.recipeIngredientService = recipeIngredientService;

    }

    // Populate dto

    private RecipeResponseDTO toResponseDTO(Recipe recipe) {

        RecipeResponseDTO dto = new RecipeResponseDTO();
        dto.setId(recipe.getId());
        dto.setName(recipe.getName());
        dto.setDescription(recipe.getDescription());
        dto.setInstructions(recipe.getInstructions());
        dto.setImageUrl(recipe.getImageUrl());

        dto.setTags(recipe.getTags().stream()
                .map(Tag::getName)
                .collect(Collectors.toList()));

        List<IngredientResponseDTO> ingredientDTOs = recipe.getIngredients().stream()
                .map(ri -> new IngredientResponseDTO(
                        ri.getIngredient().getName(),
                        ri.getQuantity(),
                        ri.getUnit(),
                        ri.getNotes()))
                .collect(Collectors.toList());
        dto.setIngredients(ingredientDTOs);

        if (recipe.getRating() != null) {
            RatingResponseDTO ratingDTO = new RatingResponseDTO();
            ratingDTO.setDifficulty(recipe.getRating().getDifficulty());
            ratingDTO.setCost(recipe.getRating().getCost());
            ratingDTO.setPrepTime(recipe.getRating().getPrepTime());
            ratingDTO.setTasteIntensity(recipe.getRating().getTasteIntensity());
            ratingDTO.setOverall(recipe.getRating().getOverall());
            dto.setRating(ratingDTO);
        }

        return dto;
    }

    @GetMapping("")
    public ResponseEntity<Page<RecipeResponseDTO>> index(@RequestParam(defaultValue = "0") int page) {

        Pageable pageable = PageRequest.of(page, 20, Sort.by("name").ascending());
        Page<Recipe> recipes = recipeService.findAll(pageable);

        return ResponseEntity.ok(recipes.map(this::toResponseDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecipeResponseDTO> show(@PathVariable int id) {

        Recipe recipe = recipeService.findById(id);
        if (recipe == null) {
            return ResponseEntity.notFound().build();
        }

        RecipeResponseDTO dto = new RecipeResponseDTO();
        dto.setId(recipe.getId());
        dto.setName(recipe.getName());
        dto.setDescription(recipe.getDescription());
        dto.setInstructions(recipe.getInstructions());
        dto.setImageUrl(recipe.getImageUrl());

        // Tags: solo i nomi, niente riferimenti circolari
        dto.setTags(recipe.getTags().stream()
                .map(Tag::getName)
                .collect(Collectors.toList()));

        // Ingredienti: nome + quantità/unità/note
        List<IngredientResponseDTO> ingredientDTOs = recipeIngredientService
                .getIngredientsForRecipe(recipe.getId())
                .stream()
                .map(ri -> new IngredientResponseDTO(
                        ri.getIngredient().getName(),
                        ri.getQuantity(),
                        ri.getUnit(),
                        ri.getNotes()))
                .collect(Collectors.toList());
        dto.setIngredients(ingredientDTOs);

        // Rating
        RecipeRating rating = recipeRatingService.findByRecipeId(recipe.getId());
        if (rating != null) {
            RatingResponseDTO ratingDTO = new RatingResponseDTO();
            ratingDTO.setDifficulty(rating.getDifficulty());
            ratingDTO.setCost(rating.getCost());
            ratingDTO.setPrepTime(rating.getPrepTime());
            ratingDTO.setTasteIntensity(rating.getTasteIntensity());
            ratingDTO.setOverall(rating.getOverall());
            dto.setRating(ratingDTO);
        }

        return ResponseEntity.ok(dto);
    }

}
