package pl.edu.agh.to.bgg.session;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.to.bgg.session.dto.GameSessionCreateDTO;
import pl.edu.agh.to.bgg.session.dto.GameSessionDetailsDTO;
import pl.edu.agh.to.bgg.session.dto.GameSessionPreviewDTO;
import pl.edu.agh.to.bgg.vote.dto.VoteCreateDTO;

import java.util.List;

@RestController
@RequestMapping("sessions")
public class GameSessionController {

    private final GameSessionService gameSessionService;

    public GameSessionController(GameSessionService gameSessionService) {
        this.gameSessionService = gameSessionService;
    }

    @GetMapping
    public List<GameSessionPreviewDTO> getSessions(@RequestParam(name = "username", required = false) String username) {
        List<GameSession> sessions;
        if (username != null) {
            sessions = gameSessionService.getUserSessions(username);
        } else {
            sessions = gameSessionService.getSessions();
        }

        return sessions.stream()
                .map(GameSessionPreviewDTO::from)
                .toList();
    }

    @GetMapping("{id}")
    public GameSessionDetailsDTO getSession(@PathVariable("id") int sessionId) {
        GameSession session = gameSessionService.getSession(sessionId);
        return GameSessionDetailsDTO.from(session);
    }

    @PatchMapping("{id}/participants")
    public GameSessionDetailsDTO addParticipantToSession(@PathVariable("id") int sessionId, @RequestHeader("X-User-Login") String username) {
        GameSession session = gameSessionService.joinSession(sessionId, username);
        return GameSessionDetailsDTO.from(session);
    }

    @DeleteMapping("{id}/participants")
    public void deleteParticipantFromSession(@PathVariable("id") int sessionId, @RequestHeader("X-User-Login") String username) {
        gameSessionService.leaveSession(sessionId, username);
    }

    @PostMapping
    public GameSessionDetailsDTO createSession(@RequestBody @Valid GameSessionCreateDTO dto, @RequestHeader("X-User-Login") String username) {
        GameSession session = gameSessionService.addSession(dto, username);
        return GameSessionDetailsDTO.from(session);
    }

    @DeleteMapping("{id}")
    public void deleteSession(@PathVariable("id") int gameSessionId, @RequestHeader("X-User-Login") String username) {
        gameSessionService.deleteGameSession(gameSessionId, username);
    }

//    @GetMapping("{id}/votes")
//    public List<Vote> getSessionVoting(@PathVariable("id") int sessionId) {
//        return gameSessionService.getSessionVoting(sessionId);
//    }

    @PatchMapping("{id}/votes")
    public void voteInSession(@PathVariable("id") int sessionId,
            @RequestHeader("X-User-Login") @NotBlank String username,
            @RequestBody @Valid VoteCreateDTO dto) {
        gameSessionService.voteForGame(username, sessionId, dto);
    }

    @PostMapping("{id}/votes")
    public void selectBoardGame(
            @PathVariable("id") int sessionId,
            @RequestHeader("X-User-Login") @NotBlank String username,
            @RequestParam("selectedBoardGameId") int selectedBoardGameId) {
        gameSessionService.selectSessionBoardGame(username, sessionId, selectedBoardGameId);
    }
}
