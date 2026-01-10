package pl.edu.agh.to.bgg.boardgame.external.geek.dto;

import pl.edu.agh.to.bgg.boardgame.external.ExternalBoardGameEntry;

public record GeekBoardGameEntryDTO (
        int objectId,
        String name,
        int yearPublished
){
    public ExternalBoardGameEntry toExternalBoardGameEntry() {
        return new ExternalBoardGameEntry(
                this.objectId,
                this.name
        );
    }
}
