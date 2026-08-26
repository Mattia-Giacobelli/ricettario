package com.example.ricettario.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ricettario.entities.PollVote;
import java.util.List;

public interface IVoteRepository extends JpaRepository<PollVote, Integer> {

    Integer countByCandidate_Id(Integer id);

    Optional<PollVote> findByPoll_IdAndCandidate_Id(Integer pollId, Integer candidateId);

    boolean existsByPoll_IdAndCandidate_Id(Integer pollId, Integer candidateId);

}
