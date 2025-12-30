package pl.edu.agh.to.bgg.session;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import pl.edu.agh.to.bgg.boardgame.BoardGame;
import pl.edu.agh.to.bgg.user.User;

@Entity
@Table(name = Voting.TABLE_NAME)
public class Voting {

    public static final String TABLE_NAME = "voting";
    public static class Columns {
        public static final String ID = "id";
        public static final String SESSION = "session";
        public static final String SESSION_PARTICIPANT = "session_participant";
        public static final String BOARD_GAME = "board_game";
        public static final String USER_WANT_GAME = "user_want_game";
        public static final String USER_KNOW_GAME = "user_know_game";
    }

    @Id
    @GeneratedValue
    @Column(name = Columns.ID)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = Columns.SESSION_PARTICIPANT, nullable = false)
    private User sessionParticipant;

    @ManyToOne(optional = false)
    @JoinColumn(name = Columns.SESSION, nullable = false)
    @JsonIgnore
    private GameSession session;

    @ManyToOne(optional = false)
    @JoinColumn(name = Columns.BOARD_GAME, nullable = false)
    private BoardGame boardGame;

    @Column(name = Columns.USER_WANT_GAME)
    private boolean userWantsGame;

    @Column(name = Columns.USER_KNOW_GAME)
    private boolean userKnowsGame;

    public Voting() {}

    public Voting(User sessionParticipant, GameSession session, BoardGame boardGame, boolean userWantsGame, boolean userKnowsGame) {
        this.sessionParticipant = sessionParticipant;
        this.session = session;
        this.boardGame = boardGame;
        this.userWantsGame = userWantsGame;
        this.userKnowsGame = userKnowsGame;
    }

    public Long getId() {
        return id;
    }

    public User getSessionParticipant() {
        return sessionParticipant;
    }

    public GameSession getSession() {
        return session;
    }

    public BoardGame getBoardGame() {
        return boardGame;
    }

    public boolean isUserWantsGame() {
        return userWantsGame;
    }

    public boolean isUserKnowsGame() {
        return userKnowsGame;
    }
}
