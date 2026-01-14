package pl.edu.agh.to.bgg.boardgame;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.edu.agh.to.bgg.boardgame.dto.BoardGameCreateDTO;
import pl.edu.agh.to.bgg.boardgame.dto.BoardGameUpdateDTO;
import pl.edu.agh.to.bgg.exception.BoardGameNotFoundException;
import pl.edu.agh.to.bgg.file.StoredFile;
import pl.edu.agh.to.bgg.file.StoredFileService;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Service
public class BoardGameService {

    private final BoardGameRepository boardGameRepository;
    private final StoredFileService storedFileService;

    public BoardGameService(BoardGameRepository boardGameRepository, StoredFileService storedFileService) {
        this.boardGameRepository = boardGameRepository;
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
            StoredFile rulebook = storedFileService.saveFile(dto.rulebookFile());
            boardGame.setRulebookFile(rulebook);
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

        updateStoredFile(dto.removeImage(), dto.imageFile(), boardGame::getImageFile, boardGame::setImageFile);
        updateStoredFile(dto.removeRulebook(), dto.rulebookFile(), boardGame::getRulebookFile, boardGame::setRulebookFile);

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
            storedFileService.deleteFile(boardGame.getRulebookFile());
            boardGameRepository.deleteById(boardGameId);
        }
    }

    private void updateStoredFile(boolean remove, MultipartFile newFile, Supplier<StoredFile> getter, Consumer<StoredFile> setter) {
        if (newFile != null && !newFile.isEmpty()) {
            storedFileService.deleteFile(getter.get());
            setter.accept(storedFileService.saveFile(newFile));
            return;
        }

        if (remove) {
            storedFileService.deleteFile(getter.get());
            setter.accept(null);
        }
    }

}
