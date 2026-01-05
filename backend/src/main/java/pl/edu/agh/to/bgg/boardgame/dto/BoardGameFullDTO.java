package pl.edu.agh.to.bgg.boardgame.dto;

import pl.edu.agh.to.bgg.boardgame.BoardGame;
public record BoardGameFullDTO(
        int id,
        String title,
        String description,
        int minPlayers,
        int maxPlayers,
        int minutesPlaytime,
        boolean discontinued,
        String imageUrl,
        String rulebookUrl
) {
    public static BoardGameFullDTO from(BoardGame game) {
        return new BoardGameFullDTO(
                game.getId(),
                game.getTitle(),
                game.getDescription(),
                game.getMinPlayers(),
                game.getMaxPlayers(),
                game.getMinutesPlaytime(),
                game.isDiscontinued(),
                game.getImageFile() != null
                        ? "/files/" + game.getImageFile().getId()
                        : null,
                game.getPdfFile() != null
                        ? "/files/" + game.getPdfFile().getId()
                        : null
        );
    }
}
