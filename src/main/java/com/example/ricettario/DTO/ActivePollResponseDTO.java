package com.example.ricettario.DTO;

import java.time.LocalDate;
import java.util.List;

import com.example.ricettario.entities.Recipe;

public class ActivePollResponseDTO {

    private Integer pollId;
    private LocalDate weekStart;
    private LocalDate weekEnd;
    private List<CandidateResponseDTO> candidates;
    private Recipe winningRecipe;

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

    public Recipe getWinningRecipe() {
        return winningRecipe;
    }

    public void setWinningRecipe(Recipe winningRecipe) {
        this.winningRecipe = winningRecipe;
    }

}