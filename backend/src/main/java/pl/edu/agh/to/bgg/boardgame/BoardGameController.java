package pl.edu.agh.to.bgg.boardgame;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.to.bgg.boardgame.dto.BoardGameCreateDTO;
import pl.edu.agh.to.bgg.boardgame.dto.BoardGameDetailsDTO;
import pl.edu.agh.to.bgg.boardgame.dto.BoardGamePreviewDTO;
import pl.edu.agh.to.bgg.boardgame.dto.BoardGameUpdateDTO;
import pl.edu.agh.to.bgg.boardgame.external.ExternalBoardGameEntry;
import pl.edu.agh.to.bgg.boardgame.external.ExternalBoardGameService;

import java.util.List;

@RestController
@RequestMapping("boardgames")
public class BoardGameController {
    private final BoardGameService boardGameService;
    private final ExternalBoardGameService externalBoardGameService;

    private final int defaultPageSize;
    private final int maxPageSize;

    public BoardGameController(BoardGameService boardGameService, ExternalBoardGameService externalBoardGameService,
                               @Value("${app.boardgame.default-pagesize}") int defaultPageSize, @Value("${app.boardgame.max-pagesize}") int maxPageSize) {
        this.boardGameService = boardGameService;
        this.externalBoardGameService = externalBoardGameService;
        this.defaultPageSize = defaultPageSize;
        this.maxPageSize = maxPageSize;
    }

//    @GetMapping
//    public List<BoardGameDetailsDTO> getBoardGames() {
//        return boardGameService.getAvailableBoardGames()
//                .stream()
//                .map(BoardGameDetailsDTO::from)
//                .toList();
//    }

    @GetMapping("previews")
    public List<BoardGamePreviewDTO> getBoardGames() {
        return boardGameService.getAvailableBoardGames()
                .stream()
                .map(BoardGamePreviewDTO::from)
                .toList();
    }

    @GetMapping()
    public Page<BoardGameDetailsDTO> getBoardGamesPaged(
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(required = false) Integer size
    ) {
        int pageSize = (size != null) ? size : defaultPageSize;

        if (pageSize > maxPageSize)
            throw new IllegalArgumentException("Max page size exceeded: " + maxPageSize);

        Page<BoardGame> boardGamesPage = boardGameService.getAvailableBoardGamesPage(page, pageSize);

        return boardGamesPage.map(BoardGameDetailsDTO::from);
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
        BoardGame boardGame = externalBoardGameService.importBoardGame(id);
        return BoardGameDetailsDTO.from(boardGame);
    }
}
