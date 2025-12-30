package pl.edu.agh.to.bgg.boardgame;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
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
        MultipartFile image,

        @Nullable
        MultipartFile pdfInstruction
) {
}
