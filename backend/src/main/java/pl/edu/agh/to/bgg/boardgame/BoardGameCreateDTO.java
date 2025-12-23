package pl.edu.agh.to.bgg.boardgame;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record BoardGameCreateDTO(
        @NotBlank(message = "Title must not be blank")
        String title,

        String description,

        @Min(value = 1, message = "minPlayers must be at least 1")
        int minPlayers,

        @Min(value = 1, message = "maxPlayers must be at least 1")
        int maxPlayers,

        @Min(value = 1, message = "minutesPlaytime must be at least 1")
        int minutesPlaytime) {

    @AssertTrue(message = "maxPlayers must be greater than or equal to minPlayers")
    public boolean isPlayersRangeValid() {
        return maxPlayers >= minPlayers;
    }
}
