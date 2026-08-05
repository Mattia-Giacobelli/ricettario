package com.example.ricettario.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.ricettario.entities.PollCandidate;
import com.example.ricettario.entities.PollVote;
import com.example.ricettario.entities.User;
import com.example.ricettario.entities.WeeklyPoll;
import com.example.ricettario.repositories.ICandidateRepository;
import com.example.ricettario.repositories.IPollVoteRepository;
import com.example.ricettario.repositories.IWeeklyPollRepository;
import com.example.ricettario.utilities.Status;

@Service
@Transactional(readOnly = true)
public class PollService {

    private final IWeeklyPollRepository pollRepository;
    private final ICandidateRepository candidateRepository;
    private final IPollVoteRepository voteRepository;

    public PollService(IWeeklyPollRepository pollRepository, ICandidateRepository candidateRepository,
            IPollVoteRepository voteRepository) {

        this.pollRepository = pollRepository;
        this.candidateRepository = candidateRepository;
        this.voteRepository = voteRepository;

    }

    public WeeklyPoll getActivePoll() {

        LocalDate weekStart = LocalDate.now();
        LocalDate weekEnd = weekStart.plusDays(6);

        return pollRepository.findByWeekStartAndWeekEnd(weekStart, weekEnd).orElseThrow();

    }

    @Transactional
    public void vote(Integer pollId, Integer candidateId, Integer userId) {

        WeeklyPoll poll = pollRepository.findById(pollId)
                .orElseThrow(() -> new RuntimeException("Poll non trovato: " + pollId));

        if (poll.getStatus() != Status.OPEN) {
            throw new IllegalStateException("Il poll non è più attivo");
        }

        PollCandidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new RuntimeException("Candidato non trovato: " + candidateId));

        if (!candidate.getPoll().getId().equals(pollId)) {
            throw new IllegalArgumentException("Il candidato non appartiene a questo poll");
        }

        voteRepository.findByPoll_IdAndUser_Id(pollId, userId).ifPresent(v -> {
            throw new IllegalStateException("Hai già votato in questo poll");
        });

        User user = new User();
        user.setId(userId);

        PollVote vote = new PollVote();
        vote.setPoll(poll);
        vote.setCandidate(candidate);
        vote.setUser(user);

        voteRepository.save(vote);
    }

}
