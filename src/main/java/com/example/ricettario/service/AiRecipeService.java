package com.example.ricettario.service;

import com.example.ricettario.DTO.AiRecipeSuggestionDTO;
import com.example.ricettario.DTO.AiRecipesDTO;
import com.example.ricettario.DTO.RecipeSuggestionRequestDTO;
import com.example.ricettario.entities.Recipe;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AiRecipeService {

    private final RestClient restClient;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RecipeService recipeService;

    @Value("${gemini.api.key}")
    private String apiKey;

    public AiRecipeService(RecipeService recipeService) {
        this.restClient = RestClient.create("https://generativelanguage.googleapis.com");
        this.recipeService = recipeService;
    }

    public List<AiRecipeSuggestionDTO> suggestRecipes(RecipeSuggestionRequestDTO request) {

        String prompt = buildPrompt(request);
        String rawResponse = callGemini(prompt);
        return parseResponse(rawResponse);
    }

    private String buildPrompt(RecipeSuggestionRequestDTO request) {

        StringBuilder sb = new StringBuilder();
        sb.append(
                "Scegli da 1 a 5, il più possibile, ricette di cucina che rispettino questi criteri o almeno simili:\n");

        if (request.getPreferredTags() != null && !request.getPreferredTags().isEmpty()) {
            sb.append("- Tag/categorie preferite: ")
                    .append(String.join(", ", request.getPreferredTags())).append("\n");
        }
        if (request.getDifficulty() != null) {
            sb.append("- Difficoltà da 1 'facile' a 5 'molto difficile': ").append(request.getDifficulty())
                    .append("\n");
        }

        List<Recipe> recipes = recipeService.findAll();

        System.out.println("Ricette recuperate da DB: " + recipes.size());
        List<AiRecipesDTO> aiRecipes = new ArrayList<>();

        recipes.stream().forEach(recipe -> {

            AiRecipesDTO newReqRecipe = new AiRecipesDTO();

            newReqRecipe.setName(recipe.getName());
            newReqRecipe.setDescription(recipe.getDescription());
            recipe.getTags().stream().forEach(tag -> {

                String tagS = tag.getName();

                newReqRecipe.getTags().add(tagS);

            });
            newReqRecipe.setdifficulty(recipe.getRating().getDifficulty());

            aiRecipes.add(newReqRecipe);

        });

        System.out.println("Ricette aggiunte al dto: " + aiRecipes.size() + aiRecipes.getFirst().getName());

        sb.append("La scelta va fatta tra queste ricette:");

        ObjectMapper objMapper = new ObjectMapper();

        try {

            String recipesJSON = objMapper.writeValueAsString(aiRecipes);

            sb.append(recipesJSON);

        } catch (Exception e) {

            throw new RuntimeException("Errore nel parsing delle ricette: " + e.getMessage(), e);

        }

        sb.append(
                """

                        Rispondi ESCLUSIVAMENTE con un array JSON valido, senza testo aggiuntivo, senza markdown, \
                        nel seguente formato esatto:
                        [
                            {
                                "name": "Nome ricetta"
                            }
                        ]
                        Se invece riscontri errori o l'array è vuoto spiegami il motivo e l'errore ESCLUSIVAMENTE con un array JSON valido, senza testo aggiuntivo, senza markdown, \
                        nel seguente formato esatto:
                        [
                            {
                                "error": "errore spiegato in breve"
                            }
                        ]
                        """);

        return sb.toString();
    }

    private String callGemini(String prompt) {

        String url = "/v1beta/models/gemini-2.5-flash:generateContent?key=" + apiKey;

        // Escapa correttamente il prompt per l'inclusione in JSON
        String escapedPrompt = prompt.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n");

        String body = """
                {
                  "contents": [{
                    "parts": [{"text": "%s"}]
                  }],
                  "generationConfig": {
                    "responseMimeType": "application/json"
                  }
                }
                """.formatted(escapedPrompt);

        return restClient.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .body(String.class);
    }

    private List<AiRecipeSuggestionDTO> parseResponse(String rawResponse) {

        try {
            JsonNode root = objectMapper.readTree(rawResponse);

            // Naviga la struttura di risposta di Gemini per estrarre il testo generato
            String generatedText = root
                    .path("candidates").get(0)
                    .path("content")
                    .path("parts").get(0)
                    .path("text")
                    .asText();

            // Il testo generato è a sua volta un JSON array (grazie a responseMimeType:
            // application/json)
            AiRecipeSuggestionDTO[] recipes = objectMapper.readValue(generatedText, AiRecipeSuggestionDTO[].class);

            return List.of(recipes);

        } catch (Exception e) {
            throw new RuntimeException("Errore nel parsing della risposta AI: " + e.getMessage(), e);
        }
    }
}
