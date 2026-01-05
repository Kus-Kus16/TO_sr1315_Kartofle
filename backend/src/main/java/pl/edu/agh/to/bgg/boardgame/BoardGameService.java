package pl.edu.agh.to.bgg.boardgame;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.edu.agh.to.bgg.boardgame.dto.BoardGameCreateDTO;
import pl.edu.agh.to.bgg.boardgame.dto.BoardGameUpdateDTO;
import pl.edu.agh.to.bgg.exception.BoardGameNotFoundException;
import pl.edu.agh.to.bgg.file.StoredFile;
import pl.edu.agh.to.bgg.file.StoredFileService;
import pl.edu.agh.to.bgg.session.GameSessionRepository;

import java.util.List;

@Service
public class BoardGameService {

    @Value("${app.image-storage-path}")
    private String imageStoragePath;

    @Value("${app.pdf-storage-path}")
    private String pdfStoragePath;

    private final BoardGameRepository boardGameRepository;
    private final GameSessionRepository gameSessionRepository;
    private final StoredFileService storedFileService;

    public BoardGameService(BoardGameRepository boardGameRepository, GameSessionRepository gameSessionRepository, StoredFileService storedFileService) {
        this.boardGameRepository = boardGameRepository;
        this.gameSessionRepository = gameSessionRepository;
        this.storedFileService = storedFileService;
    }

    public List<BoardGame> getAvailableBoardGames() {
        return boardGameRepository.findAllByDiscontinuedFalse();
    }

    public BoardGame getBoardGame(int id) {
        return boardGameRepository
                .findById(id)
                .orElseThrow(BoardGameNotFoundException::new);
    }

    @Transactional
    public BoardGame addBoardGame(BoardGameCreateDTO dto) {
        BoardGame boardGame = new BoardGame(
                dto.title(),
                dto.description(),
                dto.minPlayers(),
                dto.maxPlayers(),
                dto.minutesPlaytime()
        );

        if (dto.imageFile() != null && !dto.imageFile().isEmpty()) {
            StoredFile image = storedFileService.saveFile(dto.imageFile());
            boardGame.setImageFile(image);
        }

        if (dto.rulebookFile() != null && !dto.rulebookFile().isEmpty()) {
            StoredFile pdf = storedFileService.saveFile(dto.rulebookFile());
            boardGame.setPdfFile(pdf);
        }

        return boardGameRepository.save(boardGame);
    }


    @Transactional
    public BoardGame updateBoardGame(int id, BoardGameUpdateDTO dto) {

        BoardGame boardGame = boardGameRepository
                .findById(id)
                .orElseThrow(BoardGameNotFoundException::new);

        if (dto.description() != null && !dto.description().isBlank()) {
            boardGame.setDescription(dto.description());
        }

        if (dto.minutesPlaytime() != null) {
            boardGame.setMinutesPlaytime(dto.minutesPlaytime());
        }

        if (dto.imageFile() != null && !dto.imageFile().isEmpty()) {
            StoredFile image = storedFileService.saveFile(dto.imageFile());
            storedFileService.deleteFile(boardGame.getImageFile());
            boardGame.setImageFile(image);
        }

        if (dto.rulebookFile() != null && !dto.rulebookFile().isEmpty()) {
            StoredFile pdf = storedFileService.saveFile(dto.rulebookFile());
            storedFileService.deleteFile(boardGame.getPdfFile());
            boardGame.setPdfFile(pdf);
        }

        return boardGame;
    }

    @Transactional
    public void deleteBoardGame(int boardGameId) {
        BoardGame boardGame = boardGameRepository
                .findById(boardGameId)
                .orElseThrow(BoardGameNotFoundException::new);

        if (boardGameRepository.existsInAnySession(boardGameId)) {
            boardGame.setDiscontinued(true);
        } else {
            storedFileService.deleteFile(boardGame.getImageFile());
            storedFileService.deleteFile(boardGame.getPdfFile());
            boardGameRepository.deleteById(boardGameId);
        }
    }
}
