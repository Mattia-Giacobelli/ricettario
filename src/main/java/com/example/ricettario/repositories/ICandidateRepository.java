package com.example.ricettario.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ricettario.entities.PollCandidate;

public interface ICandidateRepository extends JpaRepository<PollCandidate, Integer> {

    List<PollCandidate> findByPoll_Id(int id);

    boolean existsByPollIdAndRecipeId(Integer pollId, Integer recipeId);

    Optional<PollCandidate> findByPollIdAndRecipeId(Integer pollId, Integer recipeId);

}
