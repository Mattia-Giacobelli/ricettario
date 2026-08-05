package com.example.ricettario.controller.api;

import com.example.ricettario.DTO.ActivePollResponseDTO;
import com.example.ricettario.DTO.AddRecipeRequestDTO;
import com.example.ricettario.DTO.CandidateResponseDTO;
import com.example.ricettario.DTO.VoteRequestDTO;
import com.example.ricettario.entities.PollCandidate;
import com.example.ricettario.entities.Recipe;
import com.example.ricettario.entities.User;
import com.example.ricettario.entities.WeeklyPoll;
import com.example.ricettario.service.CandidateService;
import com.example.ricettario.service.PollService;
import com.example.ricettario.service.RecipeService;
import com.example.ricettario.service.UserService;
import com.example.ricettario.service.VoteService;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/polls")
public class PollApiController {

    private final RecipeService recipeService;
    private final PollService pollService;
    private final UserService userService;
    private final CandidateService candidateService;
    private final VoteService voteService;

    public PollApiController(RecipeService recipeService, PollService pollService, UserService userService,
            CandidateService candidateService,
            VoteService voteService) {

        this.recipeService = recipeService;
        this.pollService = pollService;
        this.userService = userService;
        this.candidateService = candidateService;
        this.voteService = voteService;

    }

    @GetMapping("/active")
    public ResponseEntity<ActivePollResponseDTO> getActivePoll() {
        try {
            WeeklyPoll poll = pollService.getActivePoll();

            List<PollCandidate> pollCandidates = candidateService.findByPoll_Id(poll.getId());

            List<CandidateResponseDTO> candidateDTOs = pollCandidates.stream()
                    .map(c -> new CandidateResponseDTO(
                            c.getId(),
                            c.getRecipe().getId(),
                            c.getRecipe().getName(),
                            c.getRecipe().getImageUrl(),
                            voteService.countByCandidate_Id(c.getId())))
                    .collect(Collectors.toList());

            ActivePollResponseDTO newPoll = new ActivePollResponseDTO();
            newPoll.setPollId(poll.getId());
            newPoll.setWeekStart(poll.getWeekStart());
            newPoll.setWeekEnd(poll.getWeekEnd());
            newPoll.setCandidates(candidateDTOs);

            return ResponseEntity.ok(newPoll);

        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{pollId}/vote")
    public ResponseEntity<String> vote(@PathVariable Integer pollId,
            @RequestBody VoteRequestDTO voteRequest,
            Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Devi effettuare il login per votare");
        }

        String username = authentication.getName();
        User user = userService.findByUsername(username);

        try {
            pollService.vote(pollId, voteRequest.getCandidateId(), user.getId());
            return ResponseEntity.ok("Voto registrato con successo");

        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{pollId}/addrecipe")
    public ResponseEntity<?> addRecipeToPoll(
            @PathVariable Integer pollId,
            @Valid @RequestBody AddRecipeRequestDTO candidates) {

        // Check poll

        WeeklyPoll poll = pollService.getActivePoll();

        if (poll == null) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Sondaggio non trovato con ID: " + pollId));

        }

        // Check recipe
        Recipe recipe = recipeService.findById(candidates.getRecipeId());

        if (recipe == null) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Ricetta non trovata con ID: " + candidates.getRecipeId()));

        }

        // Check duplicates

        if (candidateService.existsByPollIdAndRecipeId(pollId, candidates.getRecipeId())) {

            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "La ricetta è già stata aggiunta a questo sondaggio"));

        }

        // Save recipe

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

    @DeleteMapping("/{pollID}/deleterecipe")
    public ResponseEntity<?> updateRecipe(@PathVariable Integer pollId,
            @Valid @RequestBody AddRecipeRequestDTO candidates) {

        // Check poll

        WeeklyPoll poll = pollService.getActivePoll();

        if (poll == null) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Sondaggio non trovato con ID: " + pollId));

        }

        // Check recipe
        Recipe recipe = recipeService.findById(candidates.getRecipeId());

        if (recipe == null) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Ricetta non trovata con ID: " + candidates.getRecipeId()));

        }

        // Check duplicates

        if (candidateService.existsByPollIdAndRecipeId(pollId, candidates.getRecipeId())) {

            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "La ricetta è già stata aggiunta a questo sondaggio"));

        }

        // Save recipe

        PollCandidate candidate = candidateService.findByPollIdAndRecipeId(pollId, pollId);
        candidateService.delete(candidate);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "candidateId", candidate.getId(),
                "pollId", pollId,
                "recipeId", recipe.getId(),
                "message", "Ricetta rimossa con successo dal sondaggio"));

    }

}
