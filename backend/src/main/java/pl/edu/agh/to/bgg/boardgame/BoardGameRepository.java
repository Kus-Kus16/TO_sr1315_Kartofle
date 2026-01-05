package pl.edu.agh.to.bgg.boardgame;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardGameRepository extends JpaRepository<BoardGame, Integer> {
    List<BoardGame> findAllByDiscontinuedFalse();

    @Query("SELECT COUNT(gs) > 0 FROM GameSession gs JOIN gs.boardGames bg WHERE bg.id = :boardGameId")
    boolean existsInAnySession(@Param("boardGameId") int boardGameId);
}
