package pl.edu.agh.to.bgg.session;

import jakarta.validation.Valid;
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

    @PostMapping
    public GameSession createSession(@RequestBody @Valid GameSessionCreateDTO dto) {
        return gameSessionService.addSession(dto);
    }

    @DeleteMapping("{id}")
    public void deleteSession(@PathVariable("id") int gameSessionId,
                              @RequestParam String username) {
        gameSessionService.deleteGameSession(gameSessionId, username);
    }

    @PostMapping("{id}/voting")
    public void voteInSession(
            @PathVariable("id") int sessionId,
            @RequestBody @Valid VoteRequestDTO voteDTO) {

        gameSessionService.voteForGame(voteDTO.username(), sessionId, voteDTO.boardGameId(), voteDTO.userWantsGame(), voteDTO.userKnowsGame());
    }

    @GetMapping("{id}/voting")
    public List<Voting> getSessionVoting(@PathVariable("id") int sessionId) {
        return gameSessionService.getSessionVoting(sessionId);
    }
}
