package pl.edu.agh.to.bgg.boardgame;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("boardgames")
public class BoardGameController {
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
        try {
            return boardGameService.getBoardGame(boardGameId);
        } catch (BoardGameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public BoardGame createBoardGame(@RequestBody @Valid BoardGameCreateDTO dto) {
        try {
            return boardGameService.addBoardGame(dto);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public void deleteBoardGame(@PathVariable("id") int boardGameId) {
        try {
            boardGameService.deleteBoardGame(boardGameId);
        } catch (BoardGameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
