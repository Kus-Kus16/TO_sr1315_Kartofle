package pl.edu.agh.to.bgg.boardgame;

import jakarta.persistence.*;
import pl.edu.agh.to.bgg.file.StoredFile;

import java.util.Objects;

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
        public static final String DISCONTINUED = "discontinued";
        public static final String IMAGE_FILE_ID = "image_file_id";
        public static final String RULEBOOK_FILE_ID = "rulebook_file_id";
    }

    @Id
    @GeneratedValue
    @Column(name = Columns.ID)
    private int id;

    @Column(name = Columns.TITLE, nullable = false)
    private String title;

    @Column(name = Columns.DESCRIPTION, columnDefinition = "TEXT")
    private String description;

    @Column(name = Columns.MIN_PLAYERS, nullable = false)
    private int minPlayers;

    @Column(name = Columns.MAX_PLAYERS, nullable = false)
    private int maxPlayers;

    @Column(name = Columns.MINUTES_PLAYTIME, nullable = false)
    private int minutesPlaytime;

    @Column(name = Columns.DISCONTINUED, nullable = false)
    private boolean discontinued;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = Columns.IMAGE_FILE_ID)
    private StoredFile imageFile;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = Columns.RULEBOOK_FILE_ID)
    private StoredFile rulebookFile;

    public BoardGame(String title, String description, int minPlayers, int maxPlayers, int minutesPlaytime) {
        this.title = title;
        this.description = description;
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        this.minutesPlaytime = minutesPlaytime;
        this.discontinued = false;
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

    public boolean isDiscontinued() {
        return discontinued;
    }

    public StoredFile getImageFile() {
        return imageFile;
    }

    public void setImageFile(StoredFile imageFile) {
        this.imageFile = imageFile;
    }

    public StoredFile getRulebookFile() {
        return rulebookFile;
    }

    public void setRulebookFile(StoredFile rulebookFile) {
        this.rulebookFile = rulebookFile;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setMinPlayers(int minPlayers) {
        this.minPlayers = minPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public void setMinutesPlaytime(int minutesPlaytime) {
        this.minutesPlaytime = minutesPlaytime;
    }

    public void setDiscontinued(boolean discontinued) {
        this.discontinued = discontinued;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BoardGame boardGame = (BoardGame) o;
        return id == boardGame.id && Objects.equals(title, boardGame.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title);
    }
}
