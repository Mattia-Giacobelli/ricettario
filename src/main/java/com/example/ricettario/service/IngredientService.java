package com.example.ricettario.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.ricettario.entities.Ingredient;
import com.example.ricettario.repositories.IIngredientRepository;

@Service
@Transactional(readOnly = true)
public class IngredientService {

    private final IIngredientRepository ingredientRepository;

    public IngredientService(IIngredientRepository ingredientRepository) {

        this.ingredientRepository = ingredientRepository;

    }

    public List<Ingredient> findAll() {

        return ingredientRepository.findAll();

    }

    public Page<Ingredient> findAll(Pageable pageable) {

        return ingredientRepository.findAll(pageable);

    }

    public Ingredient findById(int id) {

        return ingredientRepository.findById(id).orElseThrow();

    }

    public Optional<Ingredient> findByName(String name) {
        return ingredientRepository.findByName(name);
    }

    @Transactional
    public Ingredient create(Ingredient ingredient) {

        return ingredientRepository.save(ingredient);

    }

    @Transactional(rollbackFor = Exception.class)
    public Ingredient update(Ingredient ingredient) {

        return ingredientRepository.save(ingredient);

    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(int id) {

        ingredientRepository.deleteById(id);

    }

}
