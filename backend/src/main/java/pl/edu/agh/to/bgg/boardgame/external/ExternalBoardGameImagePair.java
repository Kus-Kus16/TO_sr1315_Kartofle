package pl.edu.agh.to.bgg.boardgame.external;

import pl.edu.agh.to.bgg.boardgame.BoardGame;

import java.util.Optional;

public record ExternalBoardGameImagePair (
        BoardGame boardGame,
        Optional<String> imageUrl
) {
}
