package com.example.ricettario.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ricettario.entities.PollVote;

public interface IVoteRepository extends JpaRepository<PollVote, Integer> {

    Integer countByCandidate_Id(int id);

}
