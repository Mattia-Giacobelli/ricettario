package com.example.ricettario.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ricettario.entities.PollSuggestionNew;

public interface INewPollSuggestionRepository extends JpaRepository<PollSuggestionNew, Integer> {

    public List<PollSuggestionNew> findByPoll_Id(Integer pollId);

}
