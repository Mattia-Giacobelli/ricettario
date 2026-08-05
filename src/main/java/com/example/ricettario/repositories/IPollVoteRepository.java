package com.example.ricettario.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ricettario.entities.PollVote;

public interface IPollVoteRepository extends JpaRepository<PollVote, Integer> {

    List<PollVote> findByCandidate_Id(Integer candidateId);

    Optional<PollVote> findByPoll_IdAndUser_Id(Integer pollId, Integer userId);

    long countByCandidate_Id(Integer candidateId);
}
