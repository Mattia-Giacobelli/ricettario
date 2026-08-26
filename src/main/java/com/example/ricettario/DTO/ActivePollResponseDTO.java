package com.example.ricettario.DTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import com.example.ricettario.entities.PollSuggestion;
import com.example.ricettario.entities.Recipe;

public class ActivePollResponseDTO {

    private Integer pollId;
    private LocalDate weekStart;
    private LocalDate weekEnd;
    private List<CandidateResponseDTO> candidates;
    private List<SuggestionResponseDTO> suggestions;
    private WinnerResponseDTO winningRecipe;

    public Integer getPollId() {
        return pollId;
    }

    public void setPollId(Integer pollId) {
        this.pollId = pollId;
    }

    public LocalDate getWeekStart() {
        return weekStart;
    }

    public void setWeekStart(LocalDate weekStart) {
        this.weekStart = weekStart;
    }

    public LocalDate getWeekEnd() {
        return weekEnd;
    }

    public void setWeekEnd(LocalDate weekEnd) {
        this.weekEnd = weekEnd;
    }

    public List<CandidateResponseDTO> getCandidates() {
        return candidates;
    }

    public void setCandidates(List<CandidateResponseDTO> candidates) {
        this.candidates = candidates;
    }

    public WinnerResponseDTO getWinningRecipe() {
        return winningRecipe;
    }

    public void setWinningRecipe(WinnerResponseDTO winningRecipe) {
        this.winningRecipe = winningRecipe;
    }

    public List<SuggestionResponseDTO> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(List<SuggestionResponseDTO> suggestions) {
        this.suggestions = suggestions;
    }

}