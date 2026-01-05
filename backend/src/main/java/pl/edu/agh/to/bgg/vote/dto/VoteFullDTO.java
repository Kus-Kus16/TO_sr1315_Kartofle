package pl.edu.agh.to.bgg.vote.dto;

import pl.edu.agh.to.bgg.vote.Vote;

public record VoteFullDTO(
        int boardGameId,
        boolean likes,
        boolean knows,
        int userId
) {
    public static VoteFullDTO from(Vote vote) {
        return new VoteFullDTO(
                vote.getBoardGame().getId(),
                vote.isLikes(),
                vote.isKnows(),
                vote.getSessionParticipant().getId()
        );
    }
}
