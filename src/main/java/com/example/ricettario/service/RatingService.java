package com.example.ricettario.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.ricettario.entities.RecipeRating;
import com.example.ricettario.repositories.IRatingRepository;

@Service
@Transactional(readOnly = true)
public class RatingService {

    private IRatingRepository ratingRepository;

    public RatingService(IRatingRepository ratingRepository) {

        this.ratingRepository = ratingRepository;

    }

    public Page<RecipeRating> findAll(Pageable pageable) {

        return ratingRepository.findAll(pageable);

    }

    public RecipeRating findById(int id) {

        return ratingRepository.findById(id).orElseThrow();

    }

    @Transactional
    public RecipeRating create(RecipeRating recipe) {

        return ratingRepository.save(recipe);

    }

}
