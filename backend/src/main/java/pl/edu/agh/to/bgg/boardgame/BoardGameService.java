package pl.edu.agh.to.bgg.boardgame;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
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

    public BoardGame getBoardGame(int id) throws BoardGameNotFoundException {
        return boardGameRepository
                .findById(id)
                .orElseThrow(BoardGameNotFoundException::new);
    }

    @Transactional
    public BoardGame addBoardGame(BoardGameCreateDTO dto) throws IllegalArgumentException, IOException {

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

        if (dto.pdfInstruction() != null && !dto.pdfInstruction().isEmpty()) {
            String path = addImageOrPdfFileToDatabaseAndGetPath(dto.pdfInstruction());
            boardGame.setPdfUrl(path);
        }

        return boardGameRepository.save(boardGame);
    }

    @Transactional
    public BoardGame updateBoardGame(int id, BoardGameUpdateDTO dto)
            throws BoardGameNotFoundException, IOException {

        BoardGame boardGame = boardGameRepository
                .findById(id)
                .orElseThrow(BoardGameNotFoundException::new);

        if (dto.description() != null && !dto.description().isBlank()) {
            boardGame.setDescription(dto.description());
        }

        if (dto.minutesPlaytime() != null) {
            boardGame.setMinutesPlaytime(dto.minutesPlaytime());
        }

        deleteFileIfExists(boardGame.getImageUrl());
        if (dto.image() != null && !dto.image().isEmpty()) {
            String path = addImageOrPdfFileToDatabaseAndGetPath(dto.image());
            boardGame.setImageUrl(path);
        }


        deleteFileIfExists(boardGame.getPdfUrl());
        if (dto.pdfInstruction() != null && !dto.pdfInstruction().isEmpty()) {
            String path = addImageOrPdfFileToDatabaseAndGetPath(dto.pdfInstruction());
            boardGame.setPdfUrl(path);
        }

        return boardGame;
    }


    @Transactional
    public void deleteBoardGame(int boardGameId) throws BoardGameNotFoundException {
        boardGameRepository
                .findById(boardGameId)
                .orElseThrow(BoardGameNotFoundException::new);

        boardGameRepository.deleteById(boardGameId);
    }

    private String addImageOrPdfFileToDatabaseAndGetPath(MultipartFile file) throws IllegalArgumentException, IOException {
        String contentType = file.getContentType();
        String storagePath;
        if (contentType == null) throw new IllegalArgumentException("Content type is null");
        if (contentType.startsWith("image/")){
            storagePath = imageStoragePath;
        }
        else if (contentType.startsWith("application/pdf")) {
            storagePath = pdfStoragePath;
        }
        else throw new IllegalArgumentException("File should be image or pdf");

        String projectRoot = new File(".").getAbsolutePath();
        projectRoot = projectRoot.substring(0, projectRoot.length() - 1);
        File imageDir = new File(storagePath);
        if (!imageDir.exists()) imageDir.mkdirs();

        String path = projectRoot + storagePath + "/" + UUID.randomUUID() + "_" + file.getOriginalFilename();
        file.transferTo(new File(path));

        return path;
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
