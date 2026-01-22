package pl.edu.agh.to.bgg.boardgame.external.geek;

import org.springframework.stereotype.Service;
import pl.edu.agh.to.bgg.boardgame.BoardGame;
import pl.edu.agh.to.bgg.boardgame.external.ExternalBoardGameEntry;
import pl.edu.agh.to.bgg.boardgame.external.ExternalBoardGameImagePair;
import pl.edu.agh.to.bgg.boardgame.external.ExternalBoardGameProvider;
import pl.edu.agh.to.bgg.boardgame.external.geek.dto.GeekSearchResponseDTO;
import pl.edu.agh.to.bgg.boardgame.external.geek.dto.GeekThingResponseDTO;
import pl.edu.agh.to.bgg.exception.BoardGameNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class GeekBoardGameProvider implements ExternalBoardGameProvider {
    private final GeekApiClient geekApiClient;

    public GeekBoardGameProvider(GeekApiClient geekApiClient) {
        this.geekApiClient = geekApiClient;
    }

    @Override
    public List<ExternalBoardGameEntry> searchFor(String query) {
        return geekApiClient.searchFor(query).searchItems()
                .stream()
                .map(item -> new ExternalBoardGameEntry(
                        item.id(),
                        item.name().value(),
                        Optional.ofNullable(item.yearpublished())
                                .map(GeekSearchResponseDTO.YearPublished::value)
                ))
                .toList();
    }

    @Override
    public ExternalBoardGameImagePair getById(int externalId) {
         GeekThingResponseDTO.ItemDetails details = geekApiClient.getById(externalId)
                .orElseThrow(BoardGameNotFoundException::new);

        BoardGame boardGame = new BoardGame(
                details.getPrimaryName().value(),
                details.description(),
                details.minplayers().value(),
                details.maxplayers().value(),
                details.playingtime().value()
        );

        return new ExternalBoardGameImagePair(
                boardGame,
                Optional.ofNullable(details.image())
        );
    }
}
