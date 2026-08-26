package com.example.ricettario.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.ricettario.entities.PollSuggestion;
import com.example.ricettario.repositories.ISuggestionRepository;

@Service
@Transactional(readOnly = true)
public class PollSuggestionService {

    private final ISuggestionRepository suggestionRepo;

    public PollSuggestionService(ISuggestionRepository suggestionRepo) {

        this.suggestionRepo = suggestionRepo;

    }

    public List<PollSuggestion> findByPoll_Id(Integer id) {

        return suggestionRepo.findByPoll_Id(id);

    }

    public boolean existsByPollIdAndRecipeId(Integer pollId, Integer recipeId) {

        return suggestionRepo.existsByPollIdAndRecipeId(pollId, recipeId);

    }

    @Transactional
    public PollSuggestion create(PollSuggestion suggestion) {

        return suggestionRepo.save(suggestion);

    }

}
