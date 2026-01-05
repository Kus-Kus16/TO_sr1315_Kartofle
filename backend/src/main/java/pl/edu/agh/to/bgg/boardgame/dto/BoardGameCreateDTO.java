package pl.edu.agh.to.bgg.boardgame.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Nullable;

public record BoardGameCreateDTO(
        @NotBlank(message = "Title must not be blank")
        String title,

        String description,

        @Min(value = 1, message = "minPlayers must be at least 1")
        int minPlayers,

        @Min(value = 1, message = "maxPlayers must be at least 1")
        int maxPlayers,

        @Min(value = 1, message = "minutesPlaytime must be at least 1")
        int minutesPlaytime,

        @Nullable
        MultipartFile imageFile,

        @Nullable
        MultipartFile rulebookFile
) {
}
