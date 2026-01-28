package pl.edu.agh.to.bgg.boardgame.dto;

import pl.edu.agh.to.bgg.boardgame.BoardGame;

public record BoardGamePreviewDTO(
        int id,
        String title,
        int minPlayers,
        int maxPlayers
) {
    public static BoardGamePreviewDTO from(BoardGame game) {
        return new BoardGamePreviewDTO(
                game.getId(),
                game.getTitle(),
                game.getMinPlayers(),
                game.getMaxPlayers()
        );
    }
}
