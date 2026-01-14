package pl.edu.agh.to.bgg.boardgame.dto;

import pl.edu.agh.to.bgg.boardgame.BoardGame;
public record BoardGameDetailsDTO(
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
    public static BoardGameDetailsDTO from(BoardGame game) {
        return new BoardGameDetailsDTO(
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
                game.getRulebookFile() != null
                        ? "/files/" + game.getRulebookFile().getId()
                        : null
        );
    }
}
