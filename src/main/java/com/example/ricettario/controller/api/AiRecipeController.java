package com.example.ricettario.controller.api;

import com.example.ricettario.DTO.AiRecipeSuggestionDTO;
import com.example.ricettario.DTO.RecipeSuggestionRequestDTO;
import com.example.ricettario.service.AiRecipeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ai")
public class AiRecipeController {

    private final AiRecipeService aiRecipeService;

    public AiRecipeController(AiRecipeService aiRecipeService) {
        this.aiRecipeService = aiRecipeService;
    }

    @PostMapping("/suggest-recipes")
    public ResponseEntity<?> suggestRecipes(@RequestBody RecipeSuggestionRequestDTO request) {
        try {
            List<AiRecipeSuggestionDTO> suggestions = aiRecipeService.suggestRecipes(request);
            return ResponseEntity.ok(suggestions);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore nella generazione delle ricette: " + e.getMessage());
        }
    }
}