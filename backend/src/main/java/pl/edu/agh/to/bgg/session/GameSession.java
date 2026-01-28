package pl.edu.agh.to.bgg.session;

import jakarta.persistence.*;
import pl.edu.agh.to.bgg.boardgame.BoardGame;
import pl.edu.agh.to.bgg.user.User;
import pl.edu.agh.to.bgg.vote.Vote;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = GameSession.TABLE_NAME)
public class GameSession {
    public static final String TABLE_NAME = "game_sessions";
    public static class Columns {
        public static final String ID = "id";
        public static final String TITLE = "title";
        public static final String DATE = "date";
        public static final String NUMBER_OF_PLAYERS = "number_of_players";
        public static final String DESCRIPTION = "description";
        public static final String OWNER_ID = "owner_id";
        public static final String SELECTED_BOARD_GAME = "selected_board_game";
    }

    public static final String PARTICIPANTS_TABLE_NAME = "participants";
    public static class ParticipantsColumns {
        public static final String SESSION_ID = "session_id";
        public static final String USER_ID = "user_id";
    }

    public static final String BOARD_GAMES_TABLE_NAME = "session_boardgames";
    public static class BoardGameIdsColumns {
        public static final String SESSION_ID = "session_id";
        public static final String BOARD_GAME_ID = "board_game_id";
    }

    @Id
    @GeneratedValue
    @Column(name = Columns.ID)
    private int id;

    @Column(name = Columns.TITLE, nullable = false)
    private String title;

    @Column(name = Columns.DATE, nullable = false)
    private LocalDate date;

    @Column(name = Columns.NUMBER_OF_PLAYERS, nullable = false)
    private int numberOfPlayers;

    @Column(name = Columns.DESCRIPTION, columnDefinition = "TEXT")
    private String description;

    @ManyToMany
    @JoinTable (
            name = BOARD_GAMES_TABLE_NAME,
            joinColumns = @JoinColumn(
                    name = BoardGameIdsColumns.SESSION_ID,
                    nullable = false
            ),
            inverseJoinColumns = @JoinColumn(
                    name = BoardGameIdsColumns.BOARD_GAME_ID,
                    nullable = false
            )
    )
    private Set<BoardGame> boardGames = new HashSet<>();

    @ManyToOne(optional = false)
    @JoinColumn(name = Columns.OWNER_ID, nullable = false)
    private User owner;

    @ManyToMany
    @JoinTable (
            name = PARTICIPANTS_TABLE_NAME,
            joinColumns = @JoinColumn(
                    name = ParticipantsColumns.SESSION_ID,
                    nullable = false
            ),
            inverseJoinColumns = @JoinColumn(
                    name = ParticipantsColumns.USER_ID,
                    nullable = false
            )
    )
    private final Set<User> participants = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = Columns.SELECTED_BOARD_GAME)
    private BoardGame selectedBoardGame;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Vote> votes = new ArrayList<>();

    public GameSession(String title, LocalDate date, int numberOfPlayers, String description, Set<BoardGame> boardGames, User owner) {
        this.title = title;
        this.date = date;
        this.numberOfPlayers = numberOfPlayers;
        this.description = description;
        this.boardGames = boardGames;
        this.owner = owner;
    }

    public GameSession() {

    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Set<BoardGame> getBoardGames() {
        return boardGames;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public String getDescription() {
        return description;
    }

    public User getOwner() {
        return owner;
    }

    public Set<User> getParticipants() {
        return participants;
    }

    public boolean votingEnded() {
        return selectedBoardGame != null;
    }

    public void setSelectedBoardGame(BoardGame boardGame) {
        selectedBoardGame = boardGame;
    }

    public List<Vote> getVotes() {
        return votes;
    }

    public BoardGame getSelectedBoardGame() {
        return selectedBoardGame;
    }

    public int getMaxMinutesPlaytime() {
        if (this.votingEnded())
            return this.getSelectedBoardGame().getMinutesPlaytime();

        return this.getBoardGames().stream()
                .mapToInt(BoardGame::getMinutesPlaytime)
                .max()
                .orElse(0);
    }
}
