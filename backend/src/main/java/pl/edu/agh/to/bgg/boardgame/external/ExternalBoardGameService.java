package pl.edu.agh.to.bgg.boardgame.external;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pl.edu.agh.to.bgg.boardgame.BoardGame;
import pl.edu.agh.to.bgg.boardgame.BoardGameService;
import pl.edu.agh.to.bgg.file.StoredFile;
import pl.edu.agh.to.bgg.file.StoredFileService;

import java.util.List;

@Service
public class ExternalBoardGameService {
    private final ExternalBoardGameProvider externalBoardGameProvider;
    private final BoardGameService boardGameService;
    private final StoredFileService storedFileService;
    private final ImageDownloadService imageDownloadService;

    public ExternalBoardGameService(ExternalBoardGameProvider externalBoardGameProvider,
                                    BoardGameService boardGameService, StoredFileService storedFileService,
                                    ImageDownloadService imageDownloadService) {

        this.externalBoardGameProvider = externalBoardGameProvider;
        this.boardGameService = boardGameService;
        this.storedFileService = storedFileService;
        this.imageDownloadService = imageDownloadService;
    }

    public List<ExternalBoardGameEntry> searchFor(String query) {
        return externalBoardGameProvider.searchFor(query);
    }

    @Transactional
    public BoardGame importBoardGame(int externalId) {
        ExternalBoardGameImagePair externalPair = externalBoardGameProvider.getById(externalId);
        BoardGame boardGame = boardGameService.saveBoardGame(externalPair.boardGame());

        externalPair.imageUrl().ifPresent(url -> {
            byte[] imageBytes = imageDownloadService.download(url);
            StoredFile imageFile = storedFileService
                    .saveFile(boardGame.getTitle(), "image/jpeg", imageBytes, imageBytes.length);
            boardGame.setImageFile(imageFile);
        });

        return boardGame;
    }
}
