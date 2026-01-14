package pl.edu.agh.to.bgg.session;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pl.edu.agh.to.bgg.boardgame.BoardGame;
import pl.edu.agh.to.bgg.boardgame.BoardGameRepository;
import pl.edu.agh.to.bgg.exception.*;
import pl.edu.agh.to.bgg.session.dto.GameSessionCreateDTO;
import pl.edu.agh.to.bgg.user.User;
import pl.edu.agh.to.bgg.user.UserRepository;
import pl.edu.agh.to.bgg.vote.Vote;
import pl.edu.agh.to.bgg.vote.dto.VoteCreateDTO;
import pl.edu.agh.to.bgg.vote.VoteRepository;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class GameSessionService {
    private final GameSessionRepository gameSessionRepository;
    private final UserRepository userRepository;
    private final BoardGameRepository boardGameRepository;
    private final VoteRepository voteRepository;

    public GameSessionService(GameSessionRepository gameSessionRepository, UserRepository userRepository, BoardGameRepository boardGameRepository, VoteRepository voteRepository) {
        this.gameSessionRepository = gameSessionRepository;
        this.userRepository = userRepository;
        this.boardGameRepository = boardGameRepository;
        this.voteRepository = voteRepository;
    }

    public List<GameSession> getSessions() {
        return gameSessionRepository.findAllWithDetails();
    }

    public GameSession getSession(int id) {
        return gameSessionRepository
                .findByIdWithDetails(id)
                .orElseThrow(GameSessionNotFoundException::new);
    }

    public List<GameSession> getUserSessions(String username) {
        return gameSessionRepository.findAllByParticipantUsername(username);
    }

    @Transactional
    public GameSession joinSession(int sessionId, String username) {
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
    public void leaveSession(int gameSessionId, String username) {

        GameSession gameSession = gameSessionRepository
                .findById(gameSessionId)
                .orElseThrow(GameSessionNotFoundException::new);

        User user = userRepository
                .findByUsername(username)
                .orElseThrow(UserNotFoundException::new);

        if (gameSession.getOwner().equals(user))
            throw new IllegalStateException("Session owner cannot leave session :)");

        gameSession.getParticipants().remove(user);

        gameSession.getVotes()
                .removeIf(vote -> Objects.equals(vote.getSessionParticipant(), user));
    }


    @Transactional
    public GameSession addSession(GameSessionCreateDTO dto, String ownerUsername) {
        if (dto.boardGamesIds().isEmpty()) {
            throw new IllegalArgumentException("Session must contain at least one board game");
        }

        Set<BoardGame> boardGames = new HashSet<>();
        int n = dto.numberOfPlayers();

        for (Integer boardGameId : dto.boardGamesIds()) {
            BoardGame boardGame = boardGameRepository
                    .findById(boardGameId)
                    .orElseThrow(BoardGameNotFoundException::new);

            boardGames.add(boardGame);

            if (n < boardGame.getMinPlayers() || n > boardGame.getMaxPlayers()) {
                throw new IllegalArgumentException(
                        "Number of players must be between " +
                                boardGame.getMinPlayers() +
                                " and " +
                                boardGame.getMaxPlayers()
                );
            }

            if (boardGame.isDiscontinued()) {
                throw new IllegalArgumentException("Board game " + boardGame.getTitle() + " no longer available");
            }
        }

        User owner = userRepository
                .findByUsername(ownerUsername)
                .orElseThrow(UserNotFoundException::new);

        GameSession gameSession = new GameSession(
                dto.title(),
                dto.date(),
                dto.numberOfPlayers(),
                dto.description(),
                boardGames,
                owner
        );

        if (boardGames.size() == 1) {
            gameSession.setSelectedBoardGame(boardGames.iterator().next());
        }

        gameSession.getParticipants().add(owner);
        gameSessionRepository.save(gameSession);
        return gameSession;
    }

    @Transactional
    public void deleteGameSession(int gameSessionId, String username) {
        GameSession session = gameSessionRepository
                .findById(gameSessionId)
                .orElseThrow(GameSessionNotFoundException::new);

        if (session.getOwner().getUsername().equals(username)) {
            gameSessionRepository.deleteById(gameSessionId);
        } else {
            throw new SecurityException("Access denied");
        }
    }

    public List<Vote> getSessionVoting(int sessionId) {
        GameSession gameSession = gameSessionRepository
                .findById(sessionId)
                .orElseThrow(GameSessionNotFoundException::new);

        return gameSession.getVotes();
    }

    @Transactional
    public void voteForGame(String username, int sessionId, VoteCreateDTO dto) {
        User user = userRepository
                .findByUsername(username)
                .orElseThrow(UserNotFoundException::new);

        GameSession gameSession = gameSessionRepository
                .findById(sessionId)
                .orElseThrow(GameSessionNotFoundException::new);

        if (gameSession.votingEnded()) {
            throw new IllegalStateException("Cannot vote after voting ended");
        }

        BoardGame boardGame = boardGameRepository
                .findById(dto.boardGameId())
                .orElseThrow(BoardGameNotFoundException::new);

        if (!gameSession.getBoardGames().contains(boardGame)) {
            throw new BoardGameNotFoundException("Board game not part of session");
        }

        Vote vote = voteRepository
                .findBySessionAndSessionParticipantAndBoardGame(gameSession, user, boardGame)
                .orElseGet(() -> new Vote(user, gameSession, boardGame));

        vote.setLikes(dto.likes());
        vote.setKnows(dto.knows());
        voteRepository.save(vote);

        gameSession.getVotes().add(vote);
    }

    @Transactional
    public void selectSessionBoardGame(String username, int sessionId, int selectedBoardGameId) {
        User user = userRepository
                .findByUsername(username)
                .orElseThrow(UserNotFoundException::new);

        GameSession gameSession = gameSessionRepository
                .findById(sessionId)
                .orElseThrow(GameSessionNotFoundException::new);

        BoardGame selectedBoardGame = boardGameRepository
                .findById(selectedBoardGameId)
                .orElseThrow(BoardGameNotFoundException::new);

        if (!gameSession.getOwner().equals(user)) {
            throw new AccessDeniedRuntimeException();
        }

        if (gameSession.votingEnded()) {
            throw new IllegalStateException("BoardGame already selected");
        }

        if (!gameSession.getBoardGames().contains(selectedBoardGame)) {
            throw new IllegalArgumentException("Selected board game not in game session");
        }

        gameSession.setSelectedBoardGame(selectedBoardGame);
    }
}
