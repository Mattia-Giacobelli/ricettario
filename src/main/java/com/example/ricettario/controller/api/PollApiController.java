package com.example.ricettario.controller.api;

import com.example.ricettario.DTO.ActivePollResponseDTO;
import com.example.ricettario.DTO.AddRecipeRequestDTO;
import com.example.ricettario.DTO.CandidateResponseDTO;
import com.example.ricettario.DTO.SuggestionResponseDTO;
import com.example.ricettario.DTO.VoteRequestDTO;
import com.example.ricettario.DTO.VoteResponseDTO;
import com.example.ricettario.DTO.WinnerResponseDTO;
import com.example.ricettario.entities.PollCandidate;
import com.example.ricettario.entities.PollSuggestion;
import com.example.ricettario.entities.PollSuggestionNew;
import com.example.ricettario.entities.PollVote;
import com.example.ricettario.entities.Recipe;
import com.example.ricettario.entities.User;
import com.example.ricettario.entities.WeeklyPoll;
import com.example.ricettario.service.CandidateService;
import com.example.ricettario.service.PollService;
import com.example.ricettario.service.PollSuggestionNewService;
import com.example.ricettario.service.PollSuggestionService;
import com.example.ricettario.service.RecipeService;
import com.example.ricettario.service.UserService;
import com.example.ricettario.service.VoteService;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/polls")
public class PollApiController {

    private final RecipeService recipeService;
    private final PollService pollService;
    private final UserService userService;
    private final CandidateService candidateService;
    private final VoteService voteService;
    private final PollSuggestionService suggestionService;
    private final PollSuggestionNewService newSuggestionService;

    public PollApiController(RecipeService recipeService, PollService pollService, UserService userService,
            CandidateService candidateService,
            VoteService voteService, PollSuggestionService suggestionService,
            PollSuggestionNewService newSuggestionService) {

        this.recipeService = recipeService;
        this.pollService = pollService;
        this.userService = userService;
        this.candidateService = candidateService;
        this.voteService = voteService;
        this.suggestionService = suggestionService;
        this.newSuggestionService = newSuggestionService;

    }

    @GetMapping("/active")
    public ResponseEntity<ActivePollResponseDTO> getActivePoll() {
        try {
            WeeklyPoll poll = pollService.getActivePoll();

            List<PollCandidate> pollCandidates = candidateService.findByPoll_Id(poll.getId());

            List<CandidateResponseDTO> candidateDTOs = pollCandidates.stream()
                    .map(c -> {

                        if (voteService.existsByPoll_IdAndCandidate_Id(poll.getId(), c.getId())) {

                            PollVote vote = voteService.findByPoll_IdAndCandidate_Id(poll.getId(), c.getId());

                            User user = vote.getUser();

                            CandidateResponseDTO newCand = new CandidateResponseDTO(
                                    c.getId(),
                                    c.getRecipe().getId(),
                                    c.getRecipe().getName(),
                                    c.getRecipe().getImageUrl(),
                                    voteService.countByCandidate_Id(c.getId()));

                            return newCand;

                        } else {

                            CandidateResponseDTO newCand = new CandidateResponseDTO(
                                    c.getId(),
                                    c.getRecipe().getId(),
                                    c.getRecipe().getName(),
                                    c.getRecipe().getImageUrl(),
                                    voteService.countByCandidate_Id(c.getId()));

                            return newCand;

                        }

                    })
                    .collect(Collectors.toList());

            List<PollSuggestion> pollSuggestions = suggestionService.findByPoll_Id(poll.getId());

            List<PollSuggestionNew> pollSuggestionsNew = newSuggestionService.findByPoll_Id(poll.getId());

            List<SuggestionResponseDTO> suggestionDTOs = pollSuggestions.stream()
                    .map(s -> new SuggestionResponseDTO(
                            s.getRecipe().getId(),
                            s.getRecipe().getName()))
                    .collect(Collectors.toList());

            List<SuggestionResponseDTO> suggestionNewDTOs = pollSuggestionsNew.stream()
                    .map(s -> new SuggestionResponseDTO(
                            s.getId(),
                            s.getName()))
                    .collect(Collectors.toList());
            suggestionNewDTOs.stream().forEach(s -> {

                suggestionDTOs.add(s);

            });

            List<PollVote> votes = voteService.findByPoll_Id(poll.getId());

            List<VoteResponseDTO> votesDTO = votes.stream()
                    .map(v -> {

                        return new VoteResponseDTO(v.getCandidate().getRecipe().getName(), v.getUser().getUsername());

                    })
                    .collect(Collectors.toList());

            ActivePollResponseDTO newPoll = new ActivePollResponseDTO();
            newPoll.setPollId(poll.getId());
            newPoll.setWeekStart(poll.getWeekStart());
            newPoll.setWeekEnd(poll.getWeekEnd());
            newPoll.setCandidates(candidateDTOs);
            newPoll.setWinningRecipe(new WinnerResponseDTO(1,
                    "none",
                    "none"));
            newPoll.setSuggestions(suggestionDTOs);
            newPoll.setVotes(votesDTO);

            return ResponseEntity.ok(newPoll);

        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/last-poll")
    public ResponseEntity<ActivePollResponseDTO> getLastPoll() {
        try {
            WeeklyPoll poll = pollService.getLastPoll();

            List<PollCandidate> pollCandidates = candidateService.findByPoll_Id(poll.getId());

            List<CandidateResponseDTO> candidateDTOs = pollCandidates.stream()
                    .map(c -> {

                        if (voteService.existsByPoll_IdAndCandidate_Id(poll.getId(), c.getId())) {

                            PollVote vote = voteService.findByPoll_IdAndCandidate_Id(poll.getId(), c.getId());

                            User user = vote.getUser();

                            CandidateResponseDTO newCand = new CandidateResponseDTO(
                                    c.getId(),
                                    c.getRecipe().getId(),
                                    c.getRecipe().getName(),
                                    c.getRecipe().getImageUrl(),
                                    voteService.countByCandidate_Id(c.getId()));

                            return newCand;

                        } else {

                            CandidateResponseDTO newCand = new CandidateResponseDTO(
                                    c.getId(),
                                    c.getRecipe().getId(),
                                    c.getRecipe().getName(),
                                    c.getRecipe().getImageUrl(),
                                    voteService.countByCandidate_Id(c.getId()));

                            return newCand;

                        }

                    })
                    .collect(Collectors.toList());

            WinnerResponseDTO winner = new WinnerResponseDTO(poll.getWinningRecipe().getId(),
                    poll.getWinningRecipe().getName(),
                    poll.getWinningRecipe().getImageUrl());

            ActivePollResponseDTO newPoll = new ActivePollResponseDTO();
            newPoll.setPollId(poll.getId());
            newPoll.setWeekStart(poll.getWeekStart());
            newPoll.setWeekEnd(poll.getWeekEnd());
            newPoll.setCandidates(candidateDTOs);
            newPoll.setWinningRecipe(winner);

            return ResponseEntity.ok(newPoll);

        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{pollId}/vote")
    public ResponseEntity<String> vote(@PathVariable Integer pollId,
            @RequestBody VoteRequestDTO voteRequest) {

        User user = userService.findByUsername(voteRequest.getUsername());

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Utente non trovato");
        }

        try {
            System.out.println(pollId);
            pollService.vote(pollId, voteRequest.getCandidateId(), user.getId());
            return ResponseEntity.ok("Voto registrato con successo");

        } catch (IllegalStateException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{pollId}/addrecipe")
    public ResponseEntity<?> addRecipeToPoll(
            @PathVariable Integer pollId,
            @Valid @RequestBody AddRecipeRequestDTO candidates) {

        WeeklyPoll poll = pollService.getActivePoll();

        if (poll == null) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Sondaggio non trovato con ID: " + pollId));

        }

        Recipe recipe = recipeService.findById(candidates.getRecipeId());

        if (recipe == null) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Ricetta non trovata con ID: " + candidates.getRecipeId()));

        }

        if (candidateService.existsByPollIdAndRecipeId(pollId, candidates.getRecipeId())) {

            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "La ricetta è già stata aggiunta a questo sondaggio"));

        }

        PollCandidate candidate = new PollCandidate();
        candidate.setPoll(poll);
        candidate.setRecipe(recipe);
        PollCandidate saved = candidateService.create(candidate);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "candidateId", saved.getId(),
                "pollId", pollId,
                "recipeId", recipe.getId(),
                "message", "Ricetta aggiunta con successo al sondaggio"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{pollId}/deleterecipe")
    public ResponseEntity<?> updateRecipe(@PathVariable Integer pollId,
            @Valid @RequestBody AddRecipeRequestDTO candidate) {

        WeeklyPoll poll = pollService.getActivePoll();

        if (poll == null) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Sondaggio non trovato con ID: " + pollId));

        }

        Recipe recipe = recipeService.findById(candidate.getRecipeId());

        if (recipe == null) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Ricetta non trovata con ID: " + candidate.getRecipeId()));

        }

        if (!candidateService.existsByPollIdAndRecipeId(pollId, candidate.getRecipeId())) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "La ricetta è già stata aggiunta a questo sondaggio"));

        }

        PollCandidate deleteCandidate = candidateService.findByPollIdAndRecipeId(pollId, recipe.getId());
        candidateService.delete(deleteCandidate);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "candidateId", deleteCandidate.getId(),
                "pollId", pollId,
                "recipeId", recipe.getId(),
                "message", "Ricetta rimossa con successo dal sondaggio"));

    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{pollId}/suggestrecipe")
    public ResponseEntity<?> addSuggestionToPoll(
            @PathVariable Integer pollId,
            @Valid @RequestBody AddRecipeRequestDTO suggestion) {

        WeeklyPoll poll = pollService.getActivePoll();

        if (poll == null) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Sondaggio non trovato con ID: " + pollId));

        }

        Recipe recipe = new Recipe();

        if (suggestion.getRecipeId() != 0) {

            recipe = recipeService.findById(suggestion.getRecipeId());

        }

        if (recipe == null) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Ricetta non trovata con ID: " + suggestion.getRecipeId()));

        }

        if (suggestionService.existsByPollIdAndRecipeId(pollId, suggestion.getRecipeId())) {

            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "La ricetta è già stata aggiunta a questo sondaggio"));

        }

        PollSuggestion saved = new PollSuggestion();
        PollSuggestionNew savedNew = new PollSuggestionNew();

        if (suggestion.getRecipeId() != 0) {

            PollSuggestion newSuggestion = new PollSuggestion();
            newSuggestion.setPoll(poll);
            newSuggestion.setRecipe(recipe);
            saved = suggestionService.create(newSuggestion);

            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "suggestionId", saved.getId(),
                    "pollId", pollId,
                    "recipeId", recipe.getId(),
                    "message", "Suggerimento aggiunto con successo al sondaggio"));

        } else {

            PollSuggestionNew newSuggestion = new PollSuggestionNew();
            newSuggestion.setPoll(poll);
            newSuggestion.setName(suggestion.getName());
            savedNew = newSuggestionService.create(newSuggestion);

            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "suggestionId", savedNew.getId(),
                    "pollId", pollId,
                    "message", "Suggerimento aggiunto con successo al sondaggio"));

        }

    }

    // @GetMapping("/votetest")
    // public ResponseEntity<?> getVoteTest() {

    // return ResponseEntity.ok("Test");

    // }

}
