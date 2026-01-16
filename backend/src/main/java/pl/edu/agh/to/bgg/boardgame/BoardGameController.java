package pl.edu.agh.to.bgg.boardgame;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.to.bgg.boardgame.dto.BoardGameCreateDTO;
import pl.edu.agh.to.bgg.boardgame.dto.BoardGameDetailsDTO;
import pl.edu.agh.to.bgg.boardgame.dto.BoardGameUpdateDTO;
import pl.edu.agh.to.bgg.boardgame.external.ExternalBoardGameEntry;
import pl.edu.agh.to.bgg.boardgame.external.ExternalBoardGameService;

import java.util.List;

@RestController
@RequestMapping("boardgames")
public class BoardGameController {
    private final BoardGameService boardGameService;
    private final ExternalBoardGameService externalBoardGameService;

    public BoardGameController(BoardGameService boardGameService, ExternalBoardGameService externalBoardGameService) {
        this.boardGameService = boardGameService;
        this.externalBoardGameService = externalBoardGameService;
    }

    @GetMapping
    public List<BoardGameDetailsDTO> getBoardGames() {
        return boardGameService.getAvailableBoardGames()
                .stream()
                .map(BoardGameDetailsDTO::from)
                .toList();
    }

    @GetMapping("paged")
    public List<BoardGameDetailsDTO> getBoardGamesPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Page<BoardGame> boardGamesPage = boardGameService.getAvailableBoardGamesPage(page, size);

        return boardGamesPage.getContent()
                .stream()
                .map(BoardGameDetailsDTO::from)
                .toList();
    }

    @GetMapping("{id}")
    public BoardGameDetailsDTO getBoardGame(@PathVariable("id") int boardGameId) {
        BoardGame boardGame = boardGameService.getBoardGame(boardGameId);
        return BoardGameDetailsDTO.from(boardGame);
    }

    @PostMapping(consumes = "multipart/form-data")
    public BoardGameDetailsDTO createBoardGame(@ModelAttribute @Valid BoardGameCreateDTO dto) {
        if (dto.minPlayers() > dto.maxPlayers()) {
            throw new IllegalArgumentException("maxPlayers must be greater than or equal to minPlayers");
        }

        BoardGame boardGame = boardGameService.addBoardGame(dto);
        return BoardGameDetailsDTO.from(boardGame);
    }

    @PatchMapping(value = "{id}", consumes = "multipart/form-data")
    public BoardGameDetailsDTO updateBoardGame(@PathVariable int id, @ModelAttribute @Valid BoardGameUpdateDTO dto) {
        BoardGame boardGame = boardGameService.updateBoardGame(id, dto);
        return BoardGameDetailsDTO.from(boardGame);
    }

    @DeleteMapping("{id}")
    public void deleteBoardGame(@PathVariable("id") int boardGameId) {
        boardGameService.tryDeleteBoardGame(boardGameId);
    }

    @GetMapping("external")
    public List<ExternalBoardGameEntry> getExternalBoardGames(@RequestParam String query) {
        return externalBoardGameService.searchFor(query);
    }

    @PostMapping("external/{id}")
    public BoardGameDetailsDTO importExternalBoardGame(@PathVariable int id) {
        try {
            BoardGame boardGame = externalBoardGameService.importBoardGame(id);
            return BoardGameDetailsDTO.from(boardGame);

        }catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
