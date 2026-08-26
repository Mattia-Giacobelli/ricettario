package com.example.ricettario.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.ricettario.entities.PollSuggestionNew;
import com.example.ricettario.repositories.INewPollSuggestionRepository;

@Service
@Transactional(readOnly = true)
public class PollSuggestionNewService {

    private final INewPollSuggestionRepository suggestionRepository;

    public PollSuggestionNewService(INewPollSuggestionRepository suggestionRepository) {

        this.suggestionRepository = suggestionRepository;

    }

    public List<PollSuggestionNew> findByPoll_Id(Integer pollId) {

        return suggestionRepository.findByPoll_Id(pollId);

    }

    @Transactional
    public PollSuggestionNew create(PollSuggestionNew newSuggestion) {

        return suggestionRepository.save(newSuggestion);

    }

}
