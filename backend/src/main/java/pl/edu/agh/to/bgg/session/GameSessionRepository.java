package pl.edu.agh.to.bgg.session;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface GameSessionRepository extends JpaRepository<GameSession, Integer> {
    @Query("SELECT DISTINCT s FROM GameSession s " +
            "JOIN s.participants p " +
            "JOIN FETCH s.owner " +
            "JOIN FETCH s.boardGames " +
            "WHERE p.username = :username")
    List<GameSession> findAllByParticipantUsername(@Param("username") String username);


    // TODO check for n+1
    @Query("SELECT DISTINCT s FROM GameSession s " +
            "LEFT JOIN FETCH s.participants " +
            "JOIN FETCH s.owner " +
            "JOIN FETCH s.boardGames " +
            "WHERE s.id = :id")
    Optional<GameSession> findByIdWithDetails(@Param("id") int id);

    // TODO change for optional
    @Query("SELECT DISTINCT s FROM GameSession s " +
            "LEFT JOIN FETCH s.participants " +
            "JOIN FETCH s.owner " +
            "JOIN FETCH s.boardGames ")
    Set<GameSession> findAllWithDetails();

    @Query("SELECT v FROM Voting v " +
            "JOIN FETCH v.boardGame " +
            "JOIN FETCH v.sessionParticipant " +
            "WHERE v.session.id = :sessionId")
    List<Voting> findVotingForSession(@Param("sessionId") int sessionId);
}
