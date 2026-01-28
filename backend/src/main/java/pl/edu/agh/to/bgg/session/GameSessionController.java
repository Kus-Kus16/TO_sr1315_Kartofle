package pl.edu.agh.to.bgg.session;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.to.bgg.boardgame.BoardGame;
import pl.edu.agh.to.bgg.boardgame.dto.BoardGameDetailsDTO;
import pl.edu.agh.to.bgg.session.dto.GameSessionCreateDTO;
import pl.edu.agh.to.bgg.session.dto.GameSessionDetailsDTO;
import pl.edu.agh.to.bgg.session.dto.GameSessionPreviewDTO;
import pl.edu.agh.to.bgg.vote.dto.VoteCreateDTO;

import java.util.List;

@RestController
@RequestMapping("sessions")
public class GameSessionController {

    private final GameSessionService gameSessionService;

    @Value("${app.gameSession.defaultPageSize}")
    private int defaultPageSize;

    @Value("${app.gameSession.maxPageSize}")
    private int maxPageSize;

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

//    @GetMapping("/filter")
//    public List<GameSessionPreviewDTO> filterSessions(
//            @RequestParam(name = "username", required = false) String username,
//            @RequestParam(name = "boardGameName", required = false) String boardGameName,
//            @RequestParam(name = "maxMinutesPlaytime", required = false) Integer maxMinutesPlaytime,
//            @RequestParam(name = "minNumberOfPlayers", required = false) Integer minNumberOfPlayers,
//            @RequestParam(name = "maxNumberOfPlayers", required = false) Integer maxNumberOfPlayers
//    ) {
//        List<GameSession> sessions = gameSessionService.getSessionsFiltered(username, boardGameName, maxMinutesPlaytime, minNumberOfPlayers, maxNumberOfPlayers);
//        return sessions.stream()
//                .map(GameSessionPreviewDTO::from)
//                .toList();
//    }

    @GetMapping("/filterWithPage")
    public Page<GameSessionPreviewDTO> filterSessionsWithPages(
            @RequestParam(name = "username", required = false) String username,
            @RequestParam(name = "boardGameName", required = false) String boardGameName,
            @RequestParam(name = "maxMinutesPlaytime", required = false) Integer maxMinutesPlaytime,
            @RequestParam(name = "minNumberOfPlayers", required = false) Integer minNumberOfPlayers,
            @RequestParam(name = "maxNumberOfPlayers", required = false) Integer maxNumberOfPlayers,
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(required = false) Integer pageSize
    ) {
        int effectivePageSize = (pageSize != null) ? pageSize : defaultPageSize;

        if (effectivePageSize > maxPageSize)
            throw new IllegalArgumentException("Max page size exceeded: " + maxPageSize);

        Page<GameSession> gameSessionsPage = gameSessionService.getFilteredSessionsPage(username, boardGameName, maxMinutesPlaytime, minNumberOfPlayers, maxNumberOfPlayers, pageNumber, effectivePageSize);

        return gameSessionsPage.map(GameSessionPreviewDTO::from);
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
