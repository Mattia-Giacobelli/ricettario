package com.example.ricettario.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ricettario.entities.PollSuggestion;
import java.util.List;
import java.util.Optional;

public interface ISuggestionRepository extends JpaRepository<PollSuggestion, Integer> {

    public List<PollSuggestion> findByPoll_Id(Integer pollId);

    public boolean existsByPollIdAndRecipeId(Integer pollId, Integer recipeId);

}
