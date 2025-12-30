package pl.edu.agh.to.bgg.session;

public record VoteRequestDTO(
        String username,
        int boardGameId,
        boolean userWantsGame,
        boolean userKnowsGame
) {}
