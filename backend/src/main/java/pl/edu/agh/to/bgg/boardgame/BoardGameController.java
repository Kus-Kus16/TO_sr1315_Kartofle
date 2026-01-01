package pl.edu.agh.to.bgg.boardgame;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import pl.edu.agh.to.bgg.exception.BoardGameNotFoundException;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("boardgames")
public class BoardGameController {
    final long IMAGE_MAX_SIZE = 2 * 1024 * 1024; // 2 MB
    final long PDF_MAX_SIZE = 5 * 1024 * 1024; // 5 MB

    private final BoardGameService boardGameService;

    public BoardGameController(BoardGameService boardGameService) {
        this.boardGameService = boardGameService;
    }

    @GetMapping
    public List<BoardGame> getBoardGames() {
        return boardGameService.getBoardGames();
    }

    @GetMapping("{id}")
    public BoardGame getBoardGame(@PathVariable("id") int boardGameId) {
        return boardGameService.getBoardGame(boardGameId);
    }

    @PostMapping(consumes = "multipart/form-data")
    public BoardGame createBoardGame(@ModelAttribute @Valid BoardGameCreateDTO dto) {
        validateBoardGameCreate(dto);
        return boardGameService.addBoardGame(dto);
    }

    @PatchMapping(value = "{id}", consumes = "multipart/form-data")
    public BoardGame updateBoardGame(
            @PathVariable int id,
            @ModelAttribute @Valid BoardGameUpdateDTO dto
    ) {
        validateBoardGameUpdate(dto);
        return boardGameService.updateBoardGame(id, dto);
    }



    @DeleteMapping("{id}")
    public void deleteBoardGame(@PathVariable("id") int boardGameId) {
        boardGameService.deleteBoardGame(boardGameId);
    }

    // validation
    private void validateBoardGameCreate(BoardGameCreateDTO dto) {
        if (dto.minPlayers() > dto.maxPlayers())
            throw new IllegalArgumentException("maxPlayers must be greater than or equal to minPlayers");

        validateImage(dto.image());
        validatePdfFile(dto.pdfRulebook());
    }

    private void validateBoardGameUpdate(BoardGameUpdateDTO dto) {
        validateImage(dto.image());
        validatePdfFile(dto.pdfRuleBook());
    }

    private void validateImage(MultipartFile image) {
        if (image != null && !image.isEmpty()) {
            String contentType = image.getContentType();

            if (image.getSize() > IMAGE_MAX_SIZE)
                throw new IllegalArgumentException("Image size should be no more than 2MB");

            if (contentType == null || !contentType.startsWith("image/")) {
                throw new IllegalArgumentException("File must be image");
            }
        }
    }

    private void validatePdfFile(MultipartFile pdfFile) {
        if (pdfFile != null && !pdfFile.isEmpty()) {
            String contentType = pdfFile.getContentType();

            if (pdfFile.getSize() > PDF_MAX_SIZE)
                throw new IllegalArgumentException("PDF size should be no more than 5MB");

            if (contentType == null || !contentType.startsWith("application/pdf")) {
                throw new IllegalArgumentException("File must be in PDF format");
            }
        }
    }
}
