package pl.edu.agh.to.bgg.session;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import pl.edu.agh.to.bgg.boardgame.BoardGame;
import pl.edu.agh.to.bgg.user.User;

public class GameSessionSpecifications {

    public static Specification<GameSession> hasParticipantUsername(String username) {
        return (gameSessionRoot, query, cb) -> {
            if (username == null || username.isBlank()) {
                return cb.conjunction();
            }

            query.distinct(true);

            Join<GameSession, User> participants = gameSessionRoot.join("participants", JoinType.LEFT);
            return cb.equal(participants.get("username"), username);
        };
    }

    public static Specification<GameSession> containsBoardGameTitle(String title) {
        return (gameSessionRoot, query, cb) -> {
            if (title == null || title.isBlank()) {
                return cb.conjunction();
            }

            String pattern = "%" + title.toLowerCase() + "%";

            query.distinct(true);

            Join<GameSession, BoardGame> selectedBoardGame = gameSessionRoot.join("selectedBoardGame", JoinType.LEFT);
            Join<GameSession, BoardGame> boardGames = gameSessionRoot.join("boardGames", JoinType.LEFT);

            var hasSelected = cb.isNotNull(gameSessionRoot.get("selectedBoardGame"));
            var selectedBoardGameMatch = cb.like(cb.lower(selectedBoardGame.get("title")), pattern);
            var boardGameMatch = cb.like(cb.lower(boardGames.get("title")), pattern);

            return cb.or(
                    cb.and(hasSelected, selectedBoardGameMatch),
                    cb.and(cb.not(hasSelected), boardGameMatch)
            );
        };
    }

    public static Specification<GameSession> hasMaximumPlaytime(Integer maxMinutesPlaytime) {
        return (gameSessionRoot, query, cb) -> {
            if (maxMinutesPlaytime == null) {
                return cb.conjunction();
            }

            query.distinct(true);

            Join<GameSession, BoardGame> selectedBoardGame = gameSessionRoot.join("selectedBoardGame", JoinType.LEFT);
            Join<GameSession, BoardGame> boardGames = gameSessionRoot.join("boardGames", JoinType.LEFT);

            var hasSelected = cb.isNotNull(gameSessionRoot.get("selectedBoardGame"));
            var selectedBoardGameMatch = cb.lessThanOrEqualTo(selectedBoardGame.get("minutesPlaytime"), maxMinutesPlaytime);
            var boardGameMatch = cb.lessThanOrEqualTo(boardGames.get("minutesPlaytime"), maxMinutesPlaytime);

            return cb.or(
                    cb.and(hasSelected, selectedBoardGameMatch),
                    cb.and(cb.not(hasSelected), boardGameMatch)
            );
        };
    }

    public static Specification<GameSession> hasMinimumPlayers(Integer minimumPlayers) {
        return (gameSessionRoot, query, cb) -> {
            if (minimumPlayers == null) {
                return cb.conjunction();
            }

            return cb.greaterThanOrEqualTo(gameSessionRoot.get("numberOfPlayers"), minimumPlayers);
        };
    }

    public static Specification<GameSession> hasMaximumPlayers(Integer maximumPlayers) {
        return (gameSessionRoot, query, cb) -> {
            if (maximumPlayers == null) {
                return cb.conjunction();
            }

            return cb.lessThanOrEqualTo(gameSessionRoot.get("numberOfPlayers"), maximumPlayers);
        };
    }
}
