package com.example.ricettario.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.ricettario.entities.PollCandidate;
import com.example.ricettario.repositories.ICandidateRepository;

@Service
public class CandidateService {

    private final ICandidateRepository candidateRepository;

    public CandidateService(ICandidateRepository candidateRepository) {

        this.candidateRepository = candidateRepository;

    }

    public List<PollCandidate> findByPoll_Id(int id) {

        return candidateRepository.findByPoll_Id(id);

    }

}
