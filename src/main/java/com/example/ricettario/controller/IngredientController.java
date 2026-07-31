package com.example.ricettario.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.ricettario.entities.Ingredient;
import com.example.ricettario.service.IngredientService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/ingredients")
public class IngredientController {

    private IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {

        this.ingredientService = ingredientService;

    }

    @GetMapping("")
    public String index(Model ingredientM, @RequestParam(defaultValue = "0") int page) {

        Pageable pageable = PageRequest.of(page, 20, Sort.by("name").ascending());
        Page<Ingredient> ingredients = ingredientService.findAll(pageable);

        ingredientM.addAttribute("ingredients", ingredients);

        return "pages/ingredients/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable int id, Model ingredientM) {

        Ingredient ingredient = ingredientService.findById(id);

        ingredientM.addAttribute("ingredient", ingredient);

        return "pages/ingredients/show";
    }

    @GetMapping("/create")
    public String createForm(Model ingredientM) {

        Ingredient newIngredient = new Ingredient();

        ingredientM.addAttribute("ingredient", newIngredient);

        return "pages/ingredients/newIngForm";
    }

    @PostMapping("/create")
    public String create(@Validated @ModelAttribute("ingredient") Ingredient ingredient,
            BindingResult result, RedirectAttributes red) {

        // System.out.println(emp);

        if (result.hasErrors()) {

            System.out.println("errore");

            return "pages/ingredients/newIngForm";

        } else {

            Ingredient newIngredient = ingredientService.create(ingredient);

            red.addFlashAttribute("msg", newIngredient.getName() + ", Progetto aggiunto correttamente");

            return "redirect:/ingredients/" + newIngredient.getId();

        }

    }

    @GetMapping("/update/{id}")
    public String updateForm(Model ingredientM, @PathVariable int id) {

        ingredientM.addAttribute("ingredient", ingredientService.findById(id));

        return "pages/ingredients/newIngForm";
    }

    @PutMapping("/{id}")
    public String updateingredient(@PathVariable Integer id,
            @Validated @ModelAttribute("ingredient") Ingredient ingredient,
            BindingResult result, RedirectAttributes red, Model ingredientM) {

        if (result.hasErrors()) {

            ingredientM.addAttribute("ingredient", ingredient);

            return "pages/ingredients/ingredientForm";

        } else {

            Ingredient oldIngredient = ingredientService.findById(ingredient.getId());

            if (oldIngredient.equals(ingredient)) {

                red.addFlashAttribute("msg", ingredient.getName() + ", Nessuna modifica apportata");

                return "redirect:/ingredients";

            }

            ingredientService.update(ingredient);

            red.addFlashAttribute("msg", ingredient.getName() + ", Ingrediente modificato correttamente");

            return "redirect:/ingredients";

        }

    }

    @DeleteMapping("/")
    public String deleteingredient(@RequestParam("id") Integer id, RedirectAttributes red) {

        Ingredient ingredient = ingredientService.findById(id);

        ingredientService.delete(id);

        red.addFlashAttribute("msg", ingredient.getName() + ", Ingrediente eliminato correttamente");

        return "redirect:/ingredients";

    }
}
