package pl.edu.agh.to.bgg.boardgame;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.edu.agh.to.bgg.exception.BoardGameNotFoundException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

@Service
public class BoardGameService {

    @Value("${app.image-storage-path}")
    private String imageStoragePath;

    @Value("${app.pdf-storage-path}")
    private String pdfStoragePath;

    private final BoardGameRepository boardGameRepository;

    public BoardGameService(BoardGameRepository boardGameRepository) {
        this.boardGameRepository = boardGameRepository;
    }

    public List<BoardGame> getBoardGames() {
        return boardGameRepository.findAll();
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

        if (dto.image() != null && !dto.image().isEmpty()) {
            String path = addImageOrPdfFileToDatabaseAndGetPath(dto.image());
            boardGame.setImageUrl(path);
        }

        if (dto.pdfRulebook() != null && !dto.pdfRulebook().isEmpty()) {
            String path = addImageOrPdfFileToDatabaseAndGetPath(dto.pdfRulebook());
            boardGame.setPdfUrl(path);
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

        // update image file
        deleteFileIfExists(boardGame.getImageUrl());
        if (dto.image() != null && !dto.image().isEmpty()) {
            String path = addImageOrPdfFileToDatabaseAndGetPath(dto.image());
            boardGame.setImageUrl(path);
        }

        // update pdf file
        deleteFileIfExists(boardGame.getPdfUrl());
        if (dto.pdfRuleBook() != null && !dto.pdfRuleBook().isEmpty()) {
            String path = addImageOrPdfFileToDatabaseAndGetPath(dto.pdfRuleBook());
            boardGame.setPdfUrl(path);
        }

        return boardGame;
    }


    @Transactional
    public void deleteBoardGame(int boardGameId) {
        boardGameRepository
                .findById(boardGameId)
                .orElseThrow(BoardGameNotFoundException::new);

        boardGameRepository.deleteById(boardGameId);
    }

    private String addImageOrPdfFileToDatabaseAndGetPath(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null) throw new IllegalArgumentException("File content type is null");

        // get file storage path
        String storageDirPath;
        if (contentType.startsWith("image/")) {
            storageDirPath = imageStoragePath;
        } else if (contentType.startsWith("application/pdf")) {
            storageDirPath = pdfStoragePath;
        } else throw new IllegalArgumentException("File should be image or pdf");

        try {
            Path projectRoot = Path.of("").toAbsolutePath();
            Path targetDir = projectRoot.resolve(storageDirPath);
            Files.createDirectories(targetDir);

            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = targetDir.resolve(fileName);

            file.transferTo(filePath);

            return filePath.toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to save file", e);
        }
    }

    private void deleteFileIfExists(String path) {
        if (path == null || path.isBlank()) return;

        File file = new File(path);
        if (file.exists() && file.isFile()) {
            boolean deleted = file.delete();
            if (!deleted) {
                System.err.println("Failed to delete file: " + path);
            }
        }
    }
}
