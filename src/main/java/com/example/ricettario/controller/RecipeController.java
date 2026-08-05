package com.example.ricettario.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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

        recipeM.addAttribute("recipe", recipeService.findById(id));

        return "pages/recipes/newRecipeForm";
    }

    @PutMapping("/{id}")
    public String update(@PathVariable String id, @Validated @ModelAttribute("recipe") Recipe recipe,
            @RequestParam(value = "image") MultipartFile img, BindingResult result, RedirectAttributes red,
            Model recipeM) throws IOException {

        if (result.hasErrors()) {

            recipeM.addAttribute("recipe", recipe);

            return "pages/recipes/newRecipeForm";

        } else {

            Recipe oldRecipe = recipeService.findById(recipe.getId());

            if (oldRecipe.equals(recipe)) {

                red.addFlashAttribute("msg", recipe.getName() + ", Nessuna modifica apportata");

                return "redirect:/recipes";

            }

            if (img != null && !img.isEmpty()) {
                deleteOldImg(oldRecipe.getImageUrl());
                recipe.setImageUrl(saveImg(img));
            }

            recipeService.update(recipe);

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
