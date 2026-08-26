package com.example.ricettario.service;

import org.springframework.stereotype.Service;

import com.example.ricettario.entities.PollVote;
import com.example.ricettario.repositories.IVoteRepository;

@Service
public class VoteService {

    private final IVoteRepository voteRepository;

    public VoteService(IVoteRepository voteRepository) {

        this.voteRepository = voteRepository;

    }

    public Integer countByCandidate_Id(int id) {

        return voteRepository.countByCandidate_Id(id);

    }

    public boolean existsByPoll_IdAndCandidate_Id(Integer pollId, Integer candidateId) {

        return voteRepository.existsByPoll_IdAndCandidate_Id(pollId, candidateId);

    }

    public PollVote findByPoll_IdAndCandidate_Id(Integer pollId, Integer candidateId) {

        return voteRepository.findByPoll_IdAndCandidate_Id(pollId, candidateId).orElseThrow();

    }

}
