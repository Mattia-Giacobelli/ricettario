package com.example.ricettario.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ricettario.entities.Ingredient;

public interface IIngredientRepository extends JpaRepository<Ingredient, Integer> {

}
