package pl.edu.agh.to.bgg.boardgame.external;

import org.springframework.stereotype.Service;
import pl.edu.agh.to.bgg.boardgame.BoardGame;
import pl.edu.agh.to.bgg.boardgame.BoardGameService;

import java.util.List;

@Service
public class ExternalBoardGameService {
    private final ExternalBoardGameProvider externalBoardGameProvider;
    private final BoardGameService boardGameService;

    public ExternalBoardGameService(ExternalBoardGameProvider externalBoardGameProvider, BoardGameService boardGameService) {
        this.externalBoardGameProvider = externalBoardGameProvider;
        this.boardGameService = boardGameService;
    }

    public List<ExternalBoardGameEntry> searchFor(String query) {
        return externalBoardGameProvider.searchFor(query);
    }

    public BoardGame importBoardGame(int externalId) {
        BoardGame externalBoardGame = externalBoardGameProvider.getById(externalId);
        return boardGameService.saveBoardGame(externalBoardGame);
    }
}
