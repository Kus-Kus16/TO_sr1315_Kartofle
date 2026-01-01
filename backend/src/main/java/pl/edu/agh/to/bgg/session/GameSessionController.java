package pl.edu.agh.to.bgg.session;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.edu.agh.to.bgg.exception.BoardGameNotFoundException;
import pl.edu.agh.to.bgg.exception.GameSessionNotFoundException;
import pl.edu.agh.to.bgg.exception.UserNotFoundException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("sessions")
public class GameSessionController {

    private final GameSessionService gameSessionService;


    public GameSessionController(GameSessionService gameSessionService) {
        this.gameSessionService = gameSessionService;
    }


    @GetMapping
        public List<GameSession> getSessions(@RequestParam(name = "username", required = false) String username) {
        if (username != null) {
            return gameSessionService.getUserSessions(username);
        }
        return new ArrayList<>(gameSessionService.getSessions());
    }


    @GetMapping("{id}")
    public GameSession getSession(@PathVariable("id") int sessionId) {
        return gameSessionService.getSession(sessionId);
    }


    @PatchMapping("{id}/participants")
    public GameSession addParticipantToSession(@PathVariable("id") int sessionId, @RequestHeader("X-User-Login") String username) {
        return gameSessionService.joinSession(sessionId, username);
    }


    @DeleteMapping("{id}/participants")
    public void deleteParticipantFromSession(@PathVariable("id") int sessionId, @RequestHeader("X-User-Login") String username) {
        gameSessionService.leaveSession(sessionId, username);
    }


    @PostMapping
    public GameSession createSession(@RequestBody @Valid GameSessionCreateDTO dto) {
        return gameSessionService.addSession(dto);
    }


    @DeleteMapping("{id}")
    public void deleteSession(@PathVariable("id") int gameSessionId,
                              @RequestParam String username) {
        gameSessionService.deleteGameSession(gameSessionId, username);
    }


    @GetMapping("{id}/voting")
    public List<Voting> getSessionVoting(@PathVariable("id") int sessionId) {
        return gameSessionService.getSessionVoting(sessionId);
    }


    @PostMapping("{id}/voting")
    public void voteInSession(
            @PathVariable("id") int sessionId,
            @RequestHeader("X-User-Login") @NotBlank String username,
            @RequestBody @Valid VoteRequestDTO voteDTO) {

        gameSessionService.voteForGame(username, sessionId, voteDTO.boardGameId(), voteDTO.userWantsGame(), voteDTO.userKnowsGame());
    }


    @PatchMapping("{id}/voting")
    public void changeVoteInSession(
            @PathVariable("id") int sessionId,
            @RequestHeader("X-User-Login") @NotBlank String username,
            @RequestBody @Valid VoteChangeDTO voteChangeDTO
    ) {

        gameSessionService.changeVote(username, voteChangeDTO.voteId(), voteChangeDTO.userWantsGame(), voteChangeDTO.userKnowsGame());
    }


    @PostMapping("{id}/voting/endVoting")
    public void endSessionVotingAndSelectBoardGame(
            @PathVariable("id") int sessionId,
            @RequestHeader("X-User-Login") @NotBlank String username,
            @RequestParam("selectedBoardGameId") int selectedBoardGameId) {
        gameSessionService.endSessionVotingAndChooseBoardGame(username, sessionId, selectedBoardGameId);
    }
}
