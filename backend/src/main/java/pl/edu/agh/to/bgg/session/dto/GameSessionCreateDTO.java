package pl.edu.agh.to.bgg.session.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record GameSessionCreateDTO (
        @NotBlank(message = "Title must not be blank")
        String title,

        @FutureOrPresent(message = "Session date cannot be in the past")
        LocalDate date,

        @Min(value = 1, message = "numberOfPlayers must be at least 1")
        int numberOfPlayers,

        String description,

        @NotNull(message = "boardGameId must not be null")
        List<@NotNull(message = "boardGameId cannot be null") Integer> boardGamesIds
){
}
