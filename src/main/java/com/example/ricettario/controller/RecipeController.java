package com.example.ricettario.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.ricettario.DTO.IngredientRowDTO;
import com.example.ricettario.DTO.RatingDTO;
import com.example.ricettario.DTO.RecipeFormDTO;
import com.example.ricettario.DTO.TagRowDTO;
import com.example.ricettario.entities.Ingredient;
import com.example.ricettario.entities.Recipe;
import com.example.ricettario.entities.RecipeIngredient;
import com.example.ricettario.entities.RecipeRating;
import com.example.ricettario.entities.Tag;
import com.example.ricettario.service.IngredientService;
import com.example.ricettario.service.RecipeIngredientService;
import com.example.ricettario.service.RecipeRatingService;
import com.example.ricettario.service.RecipeService;
import com.example.ricettario.service.TagService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@Controller
@RequestMapping("/recipes")
public class RecipeController {

    private final RecipeService recipeService;
    private final TagService tagService;
    private final IngredientService ingredientService;
    private final RecipeRatingService recipeRatingService;
    private final RecipeIngredientService recipeIngredientService;

    @Value("${app.upload.dir}")
    private String uploadDir;

    public RecipeController(RecipeService recipeService, TagService tagService, IngredientService ingredientService,
            RecipeRatingService recipeRatingService, RecipeIngredientService recipeIngredientService) {

        this.recipeService = recipeService;
        this.tagService = tagService;
        this.ingredientService = ingredientService;
        this.recipeRatingService = recipeRatingService;
        this.recipeIngredientService = recipeIngredientService;

    }

    private String saveImg(MultipartFile img) throws IOException {

        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {

            Files.createDirectories(uploadPath);

        }

        String extension = StringUtils.getFilenameExtension(img.getOriginalFilename());
        String fileName = UUID.randomUUID() + "." + extension;

        Path filePath = uploadPath.resolve(fileName);
        Files.copy(img.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return "/uploads/recipes/" + fileName;

    }

    private void deleteOldImg(String imgUrl) {

        if (imgUrl == null || imgUrl.isBlank())
            return;

        String fileName = imgUrl.substring(imgUrl.lastIndexOf('/') + 1);
        Path oldFile = Paths.get(uploadDir).resolve(fileName);

        try {

            Files.deleteIfExists(oldFile);

        } catch (IOException e) {

            System.err.println("Impossibile eliminare vecchia immagine: " + e.getMessage());

        }

    }

    @GetMapping("")
    public String index(Model recipesM, @RequestParam(defaultValue = "0") int page) {

        Pageable pageable = PageRequest.of(page, 20, Sort.by("name").ascending());
        Page<Recipe> recipes = recipeService.findAll(pageable);

        recipesM.addAttribute("recipes", recipes);

        return "pages/recipes/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable int id, Model recipeM) {

        recipeM.addAttribute("recipe", recipeService.findById(id));

        return "pages/recipes/show";
    }

    @GetMapping("/create")
    public String createForm(Model recipeM) {

        recipeM.addAttribute("recipe", new RecipeFormDTO());
        recipeM.addAttribute("allTags", tagService.findAll());
        recipeM.addAttribute("allIngredients", ingredientService.findAll());

        return "pages/recipes/newRecipeForm";

    }

    @PostMapping("/create")
    public String create(@Validated @ModelAttribute("recipe") RecipeFormDTO form,
            @RequestParam("image") MultipartFile img,
            BindingResult result, RedirectAttributes red) throws IOException {

        if (result.hasErrors()) {

            return "pages/recipes/newRecipeForm";

        } else {

            Recipe recipe = new Recipe();
            recipe.setName(form.getName());
            recipe.setDescription(form.getDescription());
            recipe.setInstructions(form.getInstructions());
            recipe.setTimesPrep(form.getTimesPrep());

            if (img != null && !img.isEmpty()) {

                recipe.setImageUrl(saveImg(img));

            }

            // Tags

            for (TagRowDTO row : form.getTags()) {

                if (row.getName() == null || row.getName().isBlank())
                    continue;

                Tag newTag = new Tag();
                newTag.setName(row.getName().trim());

                Tag tag = tagService.findByName(row.getName().trim())
                        .orElseGet(() -> tagService.create(newTag));

                recipe.getTags().add(tag);
            }

            Recipe newRecipe = recipeService.create(recipe);

            // Ingredients

            for (IngredientRowDTO row : form.getIngredients()) {
                if (row.getName() == null || row.getName().isBlank())
                    continue;

                Ingredient newIngredient = new Ingredient();
                newIngredient.setName(row.getName().trim());

                Ingredient ingredient = ingredientService.findByName(row.getName().trim())
                        .orElseGet(() -> ingredientService.create(newIngredient));

                recipeIngredientService.addIngredientToRecipe(
                        newRecipe.getId(), ingredient.getId(),
                        row.getQuantity(), row.getUnit(), row.getNotes());
            }

            // Rating
            RatingDTO rating = form.getRating();
            RecipeRating recipeRating = new RecipeRating();
            recipeRating.setRecipe(newRecipe);
            recipeRating.setDifficulty(rating.getDifficulty());
            recipeRating.setCost(rating.getCost());
            recipeRating.setPrepTime(rating.getPrepTime());
            recipeRating.setTasteIntensity(rating.getTasteIntensity());
            recipeRatingService.create(recipeRating);

            red.addFlashAttribute("msg", newRecipe.getName() + ", Ricetta aggiunta correttamente");

            return "redirect:/recipes/" + newRecipe.getId();

        }

    }

    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable int id, Model recipeM) {

        // Setup form dto

        Recipe recipe = recipeService.findById(id);
        RecipeFormDTO recipeF = new RecipeFormDTO();

        recipeF.setId(recipe.getId());
        recipeF.setName(recipe.getName());
        recipeF.setImageUrl(recipe.getImageUrl());
        recipeF.setDescription(recipe.getDescription());
        recipeF.setInstructions(recipe.getInstructions());
        recipeF.setTimesPrep(recipe.getTimesPrep());

        // Populate tagrow dto field

        for (Tag tag : recipe.getTags()) {
            TagRowDTO tagRow = new TagRowDTO();

            tagRow.setName(tag.getName());
            recipeF.getTags().add(tagRow);
        }

        // Populate tingredientRow dto field

        for (RecipeIngredient ing : recipe.getIngredients()) {
            IngredientRowDTO ingredientRow = new IngredientRowDTO();

            ingredientRow.setName(ing.getIngredient().getName());
            ingredientRow.setQuantity(ing.getQuantity());
            ingredientRow.setUnit(ing.getUnit());
            ingredientRow.setNotes(ing.getNotes());
            recipeF.getIngredients().add(ingredientRow);
        }

        // Populate ratings

        RatingDTO ratingDTO = new RatingDTO();
        RecipeRating rating = recipe.getRating();

        ratingDTO.setDifficulty(rating.getDifficulty());
        ratingDTO.setCost(rating.getCost());
        ratingDTO.setPrepTime(rating.getPrepTime());
        ratingDTO.setTasteIntensity(rating.getTasteIntensity());
        recipeF.setRating(ratingDTO);

        recipeM.addAttribute("recipe", recipeF);
        recipeM.addAttribute("allTags", tagService.findAll());
        recipeM.addAttribute("recipeTags", recipe.getTags());

        return "pages/recipes/newRecipeForm";
    }

    @PutMapping("/{id}")
    public String update(@PathVariable int id, @Validated @ModelAttribute("recipe") RecipeFormDTO recipe,
            @RequestParam("image") MultipartFile img,
            BindingResult result, Model recipeM, RedirectAttributes red) throws IOException {

        recipe.getTags().forEach(t -> System.out.println("TAG DTO ARRIVATO: " + t.getName()));

        if (result.hasErrors()) {

            recipeM.addAttribute("recipe", recipe);
            recipeM.addAttribute("allTags", tagService.findAll());
            recipeM.addAttribute("recipeTags", recipe.getTags());

            return "pages/recipes/newRecipeForm";

        } else {

            Recipe oldRecipe = recipeService.findById(id);
            oldRecipe.setName(recipe.getName());
            oldRecipe.setDescription(recipe.getDescription());
            oldRecipe.setInstructions(recipe.getInstructions());
            oldRecipe.setTimesPrep(recipe.getTimesPrep());

            if (img != null && !img.isEmpty()) {
                deleteOldImg(oldRecipe.getImageUrl());
                oldRecipe.setImageUrl(saveImg(img));
            }

            // Tags

            for (TagRowDTO row : recipe.getTags()) {

                if (row.getName() == null || row.getName().isBlank())
                    continue;

                Tag newTag = new Tag();
                newTag.setName(row.getName().trim());

                Tag tag = tagService.findByName(row.getName().trim())
                        .orElseGet(() -> tagService.create(newTag));

                System.out.println("Tag trovato: " + tag.getId() + " - " + tag.getName());

                oldRecipe.getTags().forEach(t -> System.out.println("Tag ricetta: " + t.getId() + " - " + t.getName()));

                boolean checkExists = oldRecipe.getTags().stream()
                        .anyMatch(t -> t.getName().equalsIgnoreCase(tag.getName()));

                System.out.println("checkExists = " + checkExists);

                System.out.println("Tag prima: " + oldRecipe.getTags().size());

                if (!checkExists) {
                    oldRecipe.getTags().add(tag);
                }

                System.out.println("Tag dopo: " + oldRecipe.getTags().size());

            }

            // Ingredients

            for (IngredientRowDTO row : recipe.getIngredients()) {

                if (row.getName() == null || row.getName().isBlank())
                    continue;

                Ingredient newIngredient = new Ingredient();
                newIngredient.setName(row.getName().trim());

                Ingredient ingredient = ingredientService.findByName(row.getName().trim())
                        .orElseGet(() -> ingredientService.create(newIngredient));

                recipeIngredientService.addIngredientToRecipe(
                        oldRecipe.getId(), ingredient.getId(),
                        row.getQuantity(), row.getUnit(), row.getNotes());
            }

            // Rating
            RatingDTO rating = recipe.getRating();
            RecipeRating recipeRating = new RecipeRating();
            recipeRating.setRecipeId(oldRecipe.getId());
            recipeRating.setRecipe(oldRecipe);
            recipeRating.setDifficulty(rating.getDifficulty());
            recipeRating.setCost(rating.getCost());
            recipeRating.setPrepTime(rating.getPrepTime());
            recipeRating.setTasteIntensity(rating.getTasteIntensity());
            recipeRatingService.update(recipeRating);

            if (oldRecipe.equals(oldRecipe)) {

                red.addFlashAttribute("msg", recipe.getName() + ", Nessuna modifica apportata");

                return "redirect:/recipes";

            }

            recipeService.update(oldRecipe);

            red.addFlashAttribute("msg", recipe.getName() + ", Ricetta modificata correttamente");

            return "redirect:/recipes/" + recipe.getId();

        }

    }

    @DeleteMapping("/{id}")
    public String deleteRecipe(@PathVariable int id, RedirectAttributes red) {

        Recipe recipe = recipeService.findById(id);

        deleteOldImg(recipe.getImageUrl());

        recipeService.delete(id);

        red.addFlashAttribute("msg", recipe.getName() + ", Ricetta eliminata correttamente");

        return "redirect:/recipes";

    }

}
