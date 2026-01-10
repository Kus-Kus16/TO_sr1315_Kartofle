package pl.edu.agh.to.bgg.boardgame.external;

import org.springframework.stereotype.Service;
import pl.edu.agh.to.bgg.boardgame.BoardGame;

import java.util.List;

@Service
public interface ExternalBoardGameProvider {

    List<ExternalBoardGameEntry> searchFor(String query);

    BoardGame getById(int id);
}
