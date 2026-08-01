package com.example.ricettario.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.ricettario.entities.RecipeRating;
import com.example.ricettario.service.RatingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/ratings")
public class RatingController {

    private RatingService ratingService;

    public RatingController(RatingService ratingService) {

        this.ratingService = ratingService;

    }

    @GetMapping("")
    public String index(@RequestParam(defaultValue = "0") int page, Model ratingsM) {

        Pageable pageable = PageRequest.of(page, 20, Sort.by("recipe_id").ascending());
        Page<RecipeRating> ratings = ratingService.findAll(pageable);

        ratingsM.addAttribute("ratings", ratings);

        return "pages/ratings/index";

    }

    @GetMapping("/{id}")
    public String show(@PathVariable int id, Model ratingM) {

        RecipeRating rating = ratingService.findById(id);

        ratingM.addAttribute("rating", rating);

        return "pages/ratings/show";

    }

    @GetMapping("/create")
    public String createForm(Model ratingM) {

        RecipeRating rating = new RecipeRating();

        ratingM.addAttribute("rating", rating);

        return "pages/ratings/newRatingForm";
    }

    @PostMapping("/create")
    public String createForm(@Validated @ModelAttribute("rating") RecipeRating rating, BindingResult result,
            RedirectAttributes red) {

        if (result.hasErrors()) {

            return "pages/ratings/newRatingForm";

        } else {

            RecipeRating newRating = ratingService.create(rating);

            red.addFlashAttribute("msg",
                    "Rating per la ricetta: " + newRating.getRecipe().getName() + " aggiunto correttamente");

            return "redirect:/ratings/" + newRating.getRecipeId();

        }

    }

}
