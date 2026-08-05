package com.example.ricettario.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ricettario.entities.RecipeRating;

public interface IRecipeRatingRepository extends JpaRepository<RecipeRating, Integer> {

}
