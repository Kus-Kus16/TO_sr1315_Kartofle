package pl.edu.agh.to.bgg.vote.dto;

public record VoteCreateDTO(
        int boardGameId,
        boolean likes,
        boolean knows
) {}
