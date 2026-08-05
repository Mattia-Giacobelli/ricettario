package com.example.ricettario.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ricettario.entities.Ingredient;

public interface IIngredientRepository extends JpaRepository<Ingredient, Integer> {

    public Optional<Ingredient> findByName(String name);

}
