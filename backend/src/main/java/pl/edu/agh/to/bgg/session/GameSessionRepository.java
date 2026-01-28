package pl.edu.agh.to.bgg.session;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameSessionRepository extends JpaRepository<GameSession, Integer>, JpaSpecificationExecutor<GameSession> {
    @Query("SELECT DISTINCT s FROM GameSession s " +
            "JOIN s.participants p " +
            "JOIN FETCH s.owner " +
            "JOIN FETCH s.boardGames " +
            "WHERE p.username = :username " +
            "ORDER BY s.date DESC")
    List<GameSession> findAllByParticipantUsername(@Param("username") String username);

    // TODO check for n+1
    @Query("SELECT DISTINCT s FROM GameSession s " +
            "LEFT JOIN FETCH s.participants " +
            "JOIN FETCH s.owner " +
            "JOIN FETCH s.boardGames " +
            "WHERE s.id = :id")
    Optional<GameSession> findByIdWithDetails(@Param("id") int id);

    @Query("SELECT DISTINCT s FROM GameSession s " +
            "JOIN FETCH s.owner " +
            "JOIN FETCH s.boardGames " +
            "ORDER BY s.date DESC")
    List<GameSession> findAllWithDetails();
}
