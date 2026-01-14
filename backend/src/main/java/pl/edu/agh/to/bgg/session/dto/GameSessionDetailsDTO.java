package pl.edu.agh.to.bgg.session.dto;

import pl.edu.agh.to.bgg.boardgame.BoardGame;
import pl.edu.agh.to.bgg.session.GameSession;
import pl.edu.agh.to.bgg.user.User;
import pl.edu.agh.to.bgg.vote.dto.VoteDetailsDTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public record GameSessionDetailsDTO(
        int id,
        String title,
        LocalDate date,
        int numberOfPlayers,
        String description,
        int ownerId,
        Integer selectedBoardGameId,
        List<User> participants,
        List<BoardGame> boardGames,
        List<VoteDetailsDTO> votes
) {
    public static GameSessionDetailsDTO from(GameSession session) {
        return new GameSessionDetailsDTO(
                session.getId(),
                session.getTitle(),
                session.getDate(),
                session.getNumberOfPlayers(),
                session.getDescription(),
                session.getOwner().getId(),
                session.getSelectedBoardGame() != null
                        ? session.getSelectedBoardGame().getId()
                        : null,
                new ArrayList<>(session.getParticipants()),
                new ArrayList<>(session.getBoardGames()),
                getVotes(session)
        );
    }

    private static List<VoteDetailsDTO> getVotes(GameSession session) {
        return session.getVotes().stream()
                .map(VoteDetailsDTO::from)
                .toList();
    }
}
