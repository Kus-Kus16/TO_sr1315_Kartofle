package pl.edu.agh.to.bgg.boardgame.external;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ExternalBoardGameProvider {

    List<ExternalBoardGameEntry> searchFor(String query);

    ExternalBoardGameImagePair getById(int externalId);
}
