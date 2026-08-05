package com.example.ricettario.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.ricettario.entities.PollCandidate;
import com.example.ricettario.repositories.ICandidateRepository;

@Service
@Transactional(readOnly = true)
public class CandidateService {

    private final ICandidateRepository candidateRepository;

    public CandidateService(ICandidateRepository candidateRepository) {

        this.candidateRepository = candidateRepository;

    }

    public List<PollCandidate> findByPoll_Id(int id) {

        return candidateRepository.findByPoll_Id(id);

    }

    public PollCandidate findByPollIdAndRecipeId(Integer pollId, Integer recipeId) {

        return candidateRepository.findByPollIdAndRecipeId(pollId, recipeId).orElseThrow();

    }

    public boolean existsByPollIdAndRecipeId(Integer pollId, Integer recipeId) {

        return candidateRepository.existsByPollIdAndRecipeId(pollId, recipeId);

    }

    @Transactional
    public PollCandidate create(PollCandidate candidate) {

        return candidateRepository.save(candidate);

    }

    @Transactional
    public void delete(PollCandidate candidate) {

        candidateRepository.delete(candidate);

    }

}
