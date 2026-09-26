package com.example.ricettario.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ricettario.entities.PollResultView;

public interface IPollResultViewRepository extends JpaRepository<PollResultView, Integer> {

    PollResultView findTopByPollIdOrderByVotesDesc(Integer pollId);
}
