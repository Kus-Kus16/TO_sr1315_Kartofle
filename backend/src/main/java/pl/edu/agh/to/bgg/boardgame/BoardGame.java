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
        public static final String IMAGE_URL = "image_url";
        public static final String PDF_URL = "pdf_url";
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

    @Column(name = Columns.IMAGE_URL)
    private String imageUrl;

    @Column(name = Columns.PDF_URL)
    private String pdfUrl;

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

    public String getImageUrl() {
        return imageUrl;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
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
}
