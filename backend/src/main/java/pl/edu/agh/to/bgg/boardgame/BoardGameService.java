package pl.edu.agh.to.bgg.boardgame;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardGameService {
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
    public BoardGame addBoardGame(BoardGameCreateDTO dto) throws IllegalArgumentException {
        BoardGame boardGame = new BoardGame(
                dto.title(),
                dto.description(),
                dto.minPlayers(),
                dto.maxPlayers(),
                dto.minutesPlaytime()
        );

        return boardGameRepository.save(boardGame);
    }

    @Transactional
    public void deleteBoardGame(int boardGameId) throws BoardGameNotFoundException {
        boardGameRepository
                .findById(boardGameId)
                .orElseThrow(BoardGameNotFoundException::new);

        boardGameRepository.deleteById(boardGameId);
    }
}
