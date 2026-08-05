package com.example.ricettario.DTO;

import java.time.LocalDate;
import java.util.List;

public class ActivePollResponseDTO {

    private Integer pollId;
    private LocalDate weekStart;
    private LocalDate weekEnd;
    private List<CandidateResponseDTO> candidates;

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
}