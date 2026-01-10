package pl.edu.agh.to.bgg.boardgame.external.geek.dto;

import pl.edu.agh.to.bgg.boardgame.BoardGame;

public record GeekBoardGameDetailsDTO(
        int objectId,
        int yearPublished,
        int minPlayers,
        int maxPlayers,
        int playingTime,
        int age,
        String name,
        String description,
        String thumbnail,
        String image
) {
    public BoardGame toBoardGame() {
        return new BoardGame(
                this.name,
                this.description,
                this.minPlayers,
                this.maxPlayers,
                this.playingTime
        );
    }
}
