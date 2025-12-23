package pl.edu.agh.to.bgg.session;

import jakarta.persistence.*;
import pl.edu.agh.to.bgg.boardgame.BoardGame;
import pl.edu.agh.to.bgg.user.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = GameSession.TABLE_NAME)
public class GameSession {
    public static final String TABLE_NAME = "game_sessions";
    public static class Columns {
        public static final String ID = "id";
        public static final String DATE = "date";
        public static final String NUMBER_OF_PLAYERS = "number_of_players";
        public static final String DESCRIPTION = "description";
        public static final String BOARD_GAME_ID = "board_game_id";
        public static final String OWNER_ID = "owner_id";
    }

    public static final String PARTICIPANTS_TABLE_NAME = "participants";
    public static class ParticipantsColumns {
        public static final String SESSION_ID = "session_id";
        public static final String USER_ID = "user_id";
    }

    @Id
    @GeneratedValue
    @Column(name = Columns.ID)
    private int id;

    @Column(name = Columns.DATE, nullable = false)
    private LocalDate date;

    @Column(name = Columns.NUMBER_OF_PLAYERS, nullable = false)
    private int numberOfPlayers;

    @Column(name = Columns.DESCRIPTION)
    private String description;

    @ManyToOne(optional = false)
    @JoinColumn(name = Columns.BOARD_GAME_ID, nullable = false)
    private BoardGame boardGame;

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
    private final List<User> participants = new ArrayList<>();

    public GameSession(LocalDate date, int numberOfPlayers, String description, BoardGame boardGame, User owner) {
        this.date = date;
        this.numberOfPlayers = numberOfPlayers;
        this.description = description;
        this.boardGame = boardGame;
        this.owner = owner;
    }

    public GameSession() {

    }

    public int getId() {
        return id;
    }

    public BoardGame getBoardGame() {
        return boardGame;
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

    public List<User> getParticipants() {
        return participants;
    }
}
