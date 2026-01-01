package pl.edu.agh.to.bgg.session;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pl.edu.agh.to.bgg.boardgame.BoardGame;
import pl.edu.agh.to.bgg.exception.*;
import pl.edu.agh.to.bgg.boardgame.BoardGameRepository;
import pl.edu.agh.to.bgg.user.User;
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
    private final VotingRepository votingRepository;

    public GameSessionService(GameSessionRepository gameSessionRepository, UserRepository userRepository, BoardGameRepository boardGameRepository, VotingRepository votingRepository) {
        this.gameSessionRepository = gameSessionRepository;
        this.userRepository = userRepository;
        this.boardGameRepository = boardGameRepository;
        this.votingRepository = votingRepository;
    }

    public Set<GameSession> getSessions() {
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

        gameSession.getVoting()
                .removeIf(vote -> vote.getSessionParticipant().equals(user));
    }


    @Transactional
    public GameSession addSession(GameSessionCreateDTO dto) {
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

    @Transactional
    public void voteForGame(String username, int sessionId, int boardGameId, boolean userWantsGame, boolean userKnowsGame) {
        if (username == null || username.isEmpty()) throw new IllegalArgumentException("Username cannot be empty");

        User user = userRepository
                .findByUsername(username)
                .orElseThrow(UserNotFoundException::new);

        GameSession gameSession = gameSessionRepository
                .findById(sessionId)
                .orElseThrow(GameSessionNotFoundException::new);

        BoardGame boardGame = boardGameRepository
                .findById(boardGameId)
                .orElseThrow(BoardGameNotFoundException::new);

        if (gameSession.isVotingEnded())
            throw new IllegalStateException("Cannot vote after voting ended");

        if (!gameSession.getBoardGames().contains(boardGame))
            throw new BoardGameNotFoundException("Board game not found in this game session list of games");

        boolean alreadyVoted = gameSession.getVoting().stream()
                .anyMatch(vote -> vote.getSessionParticipant().equals(user) && vote.getBoardGame().equals(boardGame));

        if (alreadyVoted) throw new IllegalStateException("User already voted for this game in this session");

        Voting vote = new Voting(user,gameSession,boardGame,userWantsGame,userKnowsGame);
        gameSession.getVoting().add(vote);
    }


    @Transactional
    public void changeVote(String username, int voteId, boolean userWantsGame, boolean userKnowsGame) {
        if (username == null || username.isEmpty()) throw new IllegalArgumentException("Username cannot be empty");

        Voting vote = votingRepository
                .findById(voteId)
                .orElseThrow(VoteNotFoundException::new);

        GameSession gameSession = vote.getSession();

        if (!vote.getSessionParticipant().getUsername().equals(username))
            throw new AccessDeniedRuntimeException();

        if (gameSession.isVotingEnded())
            throw new IllegalStateException("Cannot change vote after voting ended");

        vote.setUserKnowsGame(userKnowsGame);
        vote.setUserWantsGame(userWantsGame);
    }


    @Transactional
    public void endSessionVotingAndChooseBoardGame(String username, int sessionId, int selectedBoardGameId) {
        if (username == null || username.isEmpty()) throw new IllegalArgumentException("Username cannot be empty");

        User user = userRepository
                .findByUsername(username)
                .orElseThrow(UserNotFoundException::new);

        GameSession gameSession = gameSessionRepository
                .findById(sessionId)
                .orElseThrow(GameSessionNotFoundException::new);

        BoardGame selectedBoardGame = boardGameRepository
                .findById(selectedBoardGameId)
                .orElseThrow(BoardGameNotFoundException::new);

        if (!gameSession.getOwner().equals(user)) throw new AccessDeniedRuntimeException();
        if (gameSession.isVotingEnded()) throw new IllegalStateException("Voting already ended");
        if (!gameSession.getBoardGames().contains(selectedBoardGame)) throw new IllegalArgumentException("Selected board game not in game session");

        gameSession.endVotingAndSelectBoardGame(selectedBoardGame);
    }

    public List<Voting> getSessionVoting(int sessionId) {

        GameSession gameSession = gameSessionRepository
                .findById(sessionId)
                .orElseThrow(GameSessionNotFoundException::new);

        return gameSession.getVoting();}
}
