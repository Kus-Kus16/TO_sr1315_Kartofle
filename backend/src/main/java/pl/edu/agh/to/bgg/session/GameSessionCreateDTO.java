package pl.edu.agh.to.bgg.session;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record GameSessionCreateDTO(
        @NotNull(message = "boardGameId must not be null")
        int boardGameId,

        @NotBlank(message = "ownerUsername must not be blank")
        String ownerUsername,

        @FutureOrPresent(message = "Session date cannot be in the past")
        LocalDate date,

        @Min(value = 1, message = "numberOfPlayers must be at least 1")
        int numberOfPlayers,

        String description){
}
