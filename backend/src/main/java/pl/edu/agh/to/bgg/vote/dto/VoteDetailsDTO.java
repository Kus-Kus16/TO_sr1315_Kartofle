package pl.edu.agh.to.bgg.vote.dto;

import pl.edu.agh.to.bgg.vote.Vote;

public record VoteDetailsDTO(
        int boardGameId,
        boolean likes,
        boolean knows,
        int userId
) {
    public static VoteDetailsDTO from(Vote vote) {
        return new VoteDetailsDTO(
                vote.getBoardGame().getId(),
                vote.isLikes(),
                vote.isKnows(),
                vote.getSessionParticipant().getId()
        );
    }
}
