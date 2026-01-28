package pl.edu.agh.to.bgg.boardgame.external;

import java.util.Optional;

public record ExternalBoardGameEntry(
        int id,
        String name,
        Optional<Integer> releaseYear
){
}
