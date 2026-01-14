package pl.edu.agh.to.bgg.boardgame;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.to.bgg.boardgame.dto.BoardGameCreateDTO;
import pl.edu.agh.to.bgg.boardgame.dto.BoardGameDetailsDTO;
import pl.edu.agh.to.bgg.boardgame.dto.BoardGameUpdateDTO;

import java.util.List;

@RestController
@RequestMapping("boardgames")
public class BoardGameController {
    private final BoardGameService boardGameService;

    public BoardGameController(BoardGameService boardGameService) {
        this.boardGameService = boardGameService;
    }

    @GetMapping
    public List<BoardGameDetailsDTO> getBoardGames() {
        return boardGameService.getAvailableBoardGames()
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
        boardGameService.deleteBoardGame(boardGameId);
    }
}
