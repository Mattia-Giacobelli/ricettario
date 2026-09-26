package com.example.ricettario.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.ricettario.entities.PollCandidate;
import com.example.ricettario.entities.PollResultView;
import com.example.ricettario.entities.PollVote;
import com.example.ricettario.entities.Recipe;
import com.example.ricettario.entities.User;
import com.example.ricettario.entities.WeeklyPoll;
import com.example.ricettario.repositories.ICandidateRepository;
import com.example.ricettario.repositories.IPollResultViewRepository;
import com.example.ricettario.repositories.IPollVoteRepository;
import com.example.ricettario.repositories.IRecipeRepository;
import com.example.ricettario.repositories.IWeeklyPollRepository;
import com.example.ricettario.utilities.Status;

@Service
@Transactional(readOnly = true)
public class PollService {

    private final IWeeklyPollRepository pollRepository;
    private final ICandidateRepository candidateRepository;
    private final IPollVoteRepository voteRepository;
    private final IPollResultViewRepository pollResultViewRepository;
    private final IRecipeRepository recipeRepository;

    public PollService(IWeeklyPollRepository pollRepository, ICandidateRepository candidateRepository,
            IPollVoteRepository voteRepository, IPollResultViewRepository pollResultViewRepository,
            IRecipeRepository recipeRepository) {

        this.pollRepository = pollRepository;
        this.candidateRepository = candidateRepository;
        this.voteRepository = voteRepository;
        this.pollResultViewRepository = pollResultViewRepository;
        this.recipeRepository = recipeRepository;

    }

    public WeeklyPoll getActivePoll() {

        return pollRepository.findTopByStatusOrderByWeekEndDesc(Status.open).orElseThrow(
                () -> new RuntimeException("Nessun poll attivo per oggi"));

    }

    public WeeklyPoll getLastPoll() {

        return pollRepository.findTopByStatusOrderByWeekEndDesc(Status.closed).orElseThrow(
                () -> new RuntimeException("Nessun poll chiuso trovato per oggi"));

    }

    @Transactional
    public void vote(Integer pollId, Integer candidateId, Integer userId) {

        WeeklyPoll poll = pollRepository.findById(pollId)
                .orElseThrow(() -> new RuntimeException("Poll non trovato: " + pollId));

        if (poll.getStatus() != Status.open) {
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

    public Recipe getWinningRecipe(Integer pollId) {

        PollResultView result = pollResultViewRepository.findTopByPollIdOrderByVotesDesc(pollId);

        return recipeRepository.findById(result.getCandidateId())
                .orElseThrow(() -> new RuntimeException("Nessun vincitore"));

    }

    @Transactional
    public WeeklyPoll update(WeeklyPoll poll) {
        return pollRepository.save(poll);
    }

}
