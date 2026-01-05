package pl.edu.agh.to.bgg.session.dto;

import pl.edu.agh.to.bgg.boardgame.BoardGame;
import pl.edu.agh.to.bgg.session.GameSession;
import pl.edu.agh.to.bgg.user.User;
import pl.edu.agh.to.bgg.vote.Vote;
import pl.edu.agh.to.bgg.vote.dto.VoteFullDTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public record GameSessionFullDTO(
        int id,
        String title,
        LocalDate date,
        int numberOfPlayers,
        String description,
        int ownerId,
        Integer selectedBoardGameId,
        List<User> participants,
        List<BoardGame> boardGames,
        List<VoteFullDTO> votes
) {
    public static GameSessionFullDTO from(GameSession session) {
        return new GameSessionFullDTO(
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
                session.getVotes().stream().map(VoteFullDTO::from).toList()
        );
    }
}
