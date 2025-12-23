package pl.edu.agh.to.bgg.boardgame;

import jakarta.persistence.*;

@Entity
@Table(name = BoardGame.TABLE_NAME)
public class BoardGame {
    public static final String TABLE_NAME = "board_games";
    public static class Columns {
        public static final String ID = "id";
        public static final String TITLE = "title";
        public static final String DESCRIPTION = "description";
        public static final String MIN_PLAYERS = "min_players";
        public static final String MAX_PLAYERS = "max_players";
        public static final String MINUTES_PLAYTIME = "minutes_playtime";
    }

    @Id
    @GeneratedValue
    @Column(name = Columns.ID)
    private int id;

    @Column(name = Columns.TITLE, nullable = false)
    private String title;

    @Column(name = Columns.DESCRIPTION)
    private String description;

    @Column(name = Columns.MIN_PLAYERS, nullable = false)
    private int minPlayers;

    @Column(name = Columns.MAX_PLAYERS, nullable = false)
    private int maxPlayers;

    @Column(name = Columns.MINUTES_PLAYTIME, nullable = false)
    private int minutesPlaytime;

    public BoardGame(String title, String description, int minPlayers, int maxPlayers, int minutesPlaytime) {
        this.title = title;
        this.description = description;
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        this.minutesPlaytime = minutesPlaytime;
    }

    public BoardGame() {

    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public int getMinutesPlaytime() {
        return minutesPlaytime;
    }
}
