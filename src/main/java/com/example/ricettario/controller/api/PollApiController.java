package com.example.ricettario.controller.api;

import com.example.ricettario.DTO.ActivePollResponseDTO;
import com.example.ricettario.DTO.CandidateResponseDTO;
import com.example.ricettario.DTO.VoteRequestDTO;
import com.example.ricettario.entities.PollCandidate;
import com.example.ricettario.entities.User;
import com.example.ricettario.entities.WeeklyPoll;
import com.example.ricettario.service.CandidateService;
import com.example.ricettario.service.PollService;
import com.example.ricettario.service.UserService;
import com.example.ricettario.service.VoteService;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/polls")
public class PollApiController {

    private final PollService pollService;
    private final UserService userService;
    private final CandidateService candidateService;
    private final VoteService voteService;

    public PollApiController(PollService pollService, UserService userService, CandidateService candidateService,
            VoteService voteService) {
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
}
