package pl.edu.agh.to.bgg.boardgame;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.to.bgg.boardgame.dto.BoardGameCreateDTO;
import pl.edu.agh.to.bgg.boardgame.dto.BoardGameFullDTO;
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
    public List<BoardGameFullDTO> getBoardGames() {
        return boardGameService.getAvailableBoardGames()
                .stream()
                .map(BoardGameFullDTO::from)
                .toList();
    }

    @GetMapping("{id}")
    public BoardGameFullDTO getBoardGame(@PathVariable("id") int boardGameId) {
        BoardGame boardGame = boardGameService.getBoardGame(boardGameId);
        return BoardGameFullDTO.from(boardGame);
    }

    @PostMapping(consumes = "multipart/form-data")
    public BoardGameFullDTO createBoardGame(@ModelAttribute @Valid BoardGameCreateDTO dto) {
        System.out.println(dto);
        if (dto.minPlayers() > dto.maxPlayers()) {
            throw new IllegalArgumentException("maxPlayers must be greater than or equal to minPlayers");
        }

        try {
        BoardGame boardGame = boardGameService.addBoardGame(dto);
        return BoardGameFullDTO.from(boardGame);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @PatchMapping(value = "{id}", consumes = "multipart/form-data")
    public BoardGameFullDTO updateBoardGame(@PathVariable int id, @ModelAttribute @Valid BoardGameUpdateDTO dto) {
        BoardGame boardGame = boardGameService.updateBoardGame(id, dto);
        return BoardGameFullDTO.from(boardGame);
    }

    @DeleteMapping("{id}")
    public void deleteBoardGame(@PathVariable("id") int boardGameId) {
        boardGameService.deleteBoardGame(boardGameId);
    }
}
