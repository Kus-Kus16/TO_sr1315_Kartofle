package pl.edu.agh.to.bgg.session;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pl.edu.agh.to.bgg.boardgame.BoardGame;
import pl.edu.agh.to.bgg.boardgame.BoardGameNotFoundException;
import pl.edu.agh.to.bgg.boardgame.BoardGameRepository;
import pl.edu.agh.to.bgg.user.User;
import pl.edu.agh.to.bgg.user.UserNotFoundException;
import pl.edu.agh.to.bgg.user.UserRepository;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public Set<GameSession> getSessions() {
        return gameSessionRepository.findAllWithDetails();
    }

    public GameSession getSession(int id) throws GameSessionNotFoundException {
        return gameSessionRepository
                .findByIdWithDetails(id)
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

        if (gameSession.getDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("GameSession has already taken place");
        }

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
        if (dto.boardGameIds().isEmpty()) throw new IllegalArgumentException("Session must contain at least one board game");

        Set<BoardGame> boardGames = new HashSet<>();
        
        for (Integer boardGameId : dto.boardGameIds()) {
            BoardGame boardGame = boardGameRepository
                    .findById(boardGameId)
                    .orElseThrow(BoardGameNotFoundException::new);
            boardGames.add(boardGame);

            int n = dto.numberOfPlayers();
            if (n < boardGame.getMinPlayers() || n > boardGame.getMaxPlayers()) {
                throw new IllegalArgumentException(
                        "Number of players must be between " +
                                boardGame.getMinPlayers() +
                                " and " +
                                boardGame.getMaxPlayers()
                );
            }
        }

        User owner = userRepository
                .findByUsername(dto.ownerUsername())
                .orElseThrow(UserNotFoundException::new);

        GameSession gameSession = new GameSession(
                dto.title(),
                dto.date(),
                dto.numberOfPlayers(),
                dto.description(),
                boardGames,
                owner
        );

        gameSession.getParticipants().add(owner);
        gameSessionRepository.save(gameSession);
        return gameSession;
    }

    public void deleteGameSession(int gameSessionId) throws GameSessionNotFoundException {
        gameSessionRepository
                .findById(gameSessionId)
                .orElseThrow(GameSessionNotFoundException::new);

        gameSessionRepository.deleteById(gameSessionId);
    }

    @Transactional
    public void voteForGame(String username, int sessionId, int boardGameId, boolean userWantsGame, boolean userKnowsGame) {
        if (username == null || username.isEmpty()) throw new IllegalArgumentException("Username cannot be empty");

        User user = userRepository
                .findByUsername(username)
                .orElseThrow(IllegalArgumentException::new);

        GameSession gameSession = gameSessionRepository
                .findById(sessionId)
                .orElseThrow(IllegalArgumentException::new);

        BoardGame boardGame = boardGameRepository
                .findById(boardGameId)
                .orElseThrow(IllegalArgumentException::new);

        Voting vote = new Voting(user,gameSession,boardGame,userWantsGame,userKnowsGame);

        gameSession.getVoting().add(vote);
    }

    public List<Voting> getSessionVoting(int sessionId) {
        return gameSessionRepository
                .findVotingForSession(sessionId);
    }
}
