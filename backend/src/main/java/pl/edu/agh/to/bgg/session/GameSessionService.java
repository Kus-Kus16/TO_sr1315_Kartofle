package pl.edu.agh.to.bgg.session;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import pl.edu.agh.to.bgg.boardgame.BoardGame;
import pl.edu.agh.to.bgg.boardgame.BoardGameNotFoundException;
import pl.edu.agh.to.bgg.boardgame.BoardGameRepository;
import pl.edu.agh.to.bgg.user.User;
import pl.edu.agh.to.bgg.user.UserNotFoundException;
import pl.edu.agh.to.bgg.user.UserRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class GameSessionService {
    private final GameSessionRepository gameSessionRepository;
    private final UserRepository userRepository;
    private final BoardGameRepository boardGameRepository;

    public GameSessionService(GameSessionRepository gameSessionRepository, UserRepository userRepository, BoardGameRepository boardGameRepository) {
        this.gameSessionRepository = gameSessionRepository;
        this.userRepository = userRepository;
        this.boardGameRepository = boardGameRepository;
    }

    public List<GameSession> getSessions() {
        return gameSessionRepository.findAll();
    }

    public GameSession getSession(int id) throws GameSessionNotFoundException {
        return gameSessionRepository
                .findById(id)
                .orElseThrow(GameSessionNotFoundException::new);
    }

    public List<GameSession> getUserSessions(String username) {
        return gameSessionRepository.findAllByParticipantUsername(username);
    }

    @Transactional
    public GameSession joinSession(int sessionId, String username)
            throws GameSessionNotFoundException, UserNotFoundException {

        GameSession gameSession = gameSessionRepository
                .findByIdWithDetails(sessionId)
                .orElseThrow(GameSessionNotFoundException::new);

        User owner = gameSession.getOwner();

        if (owner.getUsername().equals(username)) {
            throw new IllegalArgumentException("Cannot join owned gameSession");
        }

        if (gameSession.getNumberOfPlayers() == gameSession.getParticipants().size()) {
            throw new IllegalArgumentException("GameSession is full");
        }

        User user = userRepository
                .findByUsername(username)
                .orElseThrow(UserNotFoundException::new);

        if (gameSession.getParticipants().contains(user)) {
            throw new IllegalArgumentException("Already joined gameSession");
        }

        gameSession.getParticipants().add(user);

        return gameSession;
    }

    @Transactional
    public GameSession addSession(GameSessionCreateDTO dto) throws BoardGameNotFoundException, UserNotFoundException {
        BoardGame boardGame = boardGameRepository
                .findById(dto.boardGameId())
                .orElseThrow(BoardGameNotFoundException::new);

        int n = dto.numberOfPlayers();
        if (n < boardGame.getMinPlayers() || n > boardGame.getMaxPlayers()) {
            throw new IllegalArgumentException(
                    "Number of players must be between " +
                    boardGame.getMinPlayers() +
                    " and " +
                    boardGame.getMaxPlayers()
            );
        }

        User owner = userRepository
                .findByUsername(dto.ownerUsername())
                .orElseThrow(UserNotFoundException::new);

        GameSession gameSession = new GameSession(
                dto.date(),
                dto.numberOfPlayers(),
                dto.description(),
                boardGame,
                owner
        );

        gameSession.getParticipants().add(owner);
        gameSessionRepository.save(gameSession);
        return gameSession;
    }
}
