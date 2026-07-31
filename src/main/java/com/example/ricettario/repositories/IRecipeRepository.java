package com.example.ricettario.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ricettario.entities.Recipe;

public interface IRecipeRepository extends JpaRepository<Recipe, Integer> {

}
