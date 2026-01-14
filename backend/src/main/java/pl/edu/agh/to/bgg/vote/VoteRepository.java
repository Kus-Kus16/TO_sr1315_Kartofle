package pl.edu.agh.to.bgg.vote;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.agh.to.bgg.boardgame.BoardGame;
import pl.edu.agh.to.bgg.session.GameSession;
import pl.edu.agh.to.bgg.user.User;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Integer> {
    Optional<Vote> findBySessionAndSessionParticipantAndBoardGame(
            GameSession session,
            User sessionParticipant,
            BoardGame boardGame
    );
}
