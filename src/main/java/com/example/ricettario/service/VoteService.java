package com.example.ricettario.service;

import java.util.List;

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

    public List<PollVote> findByPoll_Id(Integer pollId) {

        return voteRepository.findByPoll_Id(pollId);

    }

    public boolean existsByPoll_IdAndCandidate_Id(Integer pollId, Integer candidateId) {

        return voteRepository.existsByPoll_IdAndCandidate_Id(pollId, candidateId);

    }

    public PollVote findByPoll_IdAndCandidate_Id(Integer pollId, Integer candidateId) {

        return voteRepository.findByPoll_IdAndCandidate_Id(pollId, candidateId).orElseThrow();

    }

}
