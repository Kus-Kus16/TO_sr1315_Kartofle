package pl.edu.agh.to.bgg.session.dto;

import pl.edu.agh.to.bgg.boardgame.dto.BoardGameDetailsDTO;
import pl.edu.agh.to.bgg.session.GameSession;
import pl.edu.agh.to.bgg.user.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public record GameSessionPreviewDTO(
        int id,
        String title,
        LocalDate date,
        int numberOfPlayers,
        String description,
        int ownerId,
        BoardGameDetailsDTO selectedBoardGame,
        List<User> participants
) {
    public static GameSessionPreviewDTO from(GameSession session) {
        return new GameSessionPreviewDTO(
                session.getId(),
                session.getTitle(),
                session.getDate(),
                session.getNumberOfPlayers(),
                session.getDescription(),
                session.getOwner().getId(),
                session.getSelectedBoardGame() != null
                        ? BoardGameDetailsDTO.from(session.getSelectedBoardGame())
                        : null,
                new ArrayList<>(session.getParticipants())
        );
    }
}
