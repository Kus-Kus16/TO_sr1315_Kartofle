package pl.edu.agh.to.bgg.vote;

import jakarta.persistence.*;
import pl.edu.agh.to.bgg.boardgame.BoardGame;
import pl.edu.agh.to.bgg.session.GameSession;
import pl.edu.agh.to.bgg.user.User;

@Entity
@Table(name = Vote.TABLE_NAME)
public class Vote {
    public static final String TABLE_NAME = "votes";
    public static class Columns {
        public static final String ID = "id";
        public static final String SESSION = "session";
        public static final String SESSION_PARTICIPANT = "session_participant";
        public static final String BOARD_GAME = "board_game";
        public static final String LIKES = "likes";
        public static final String KNOWS = "knows";
    }

    @Id
    @GeneratedValue
    @Column(name = Columns.ID)
    private int id;

    @ManyToOne(optional = false)
    @JoinColumn(name = Columns.SESSION_PARTICIPANT, nullable = false)
    private User sessionParticipant;

    @ManyToOne(optional = false)
    @JoinColumn(name = Columns.SESSION, nullable = false)
    private GameSession session;

    @ManyToOne(optional = false)
    @JoinColumn(name = Columns.BOARD_GAME, nullable = false)
    private BoardGame boardGame;

    @Column(name = Columns.LIKES)
    private boolean likes;

    @Column(name = Columns.KNOWS)
    private boolean knows;

    public Vote() {}

    public Vote(User sessionParticipant, GameSession session, BoardGame boardGame) {
        this.sessionParticipant = sessionParticipant;
        this.session = session;
        this.boardGame = boardGame;
    }

    public int getId() {
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

    public boolean isLikes() {
        return likes;
    }

    public boolean isKnows() {
        return knows;
    }

    public void setLikes(boolean userWantsGame) {
        this.likes = userWantsGame;
    }

    public void setKnows(boolean userKnowsGame) {
        this.knows = userKnowsGame;
    }
}
