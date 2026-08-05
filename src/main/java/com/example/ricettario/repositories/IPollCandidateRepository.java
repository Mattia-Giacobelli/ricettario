package com.example.ricettario.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ricettario.entities.PollCandidate;

public interface IPollCandidateRepository extends JpaRepository<PollCandidate, Integer> {

}
