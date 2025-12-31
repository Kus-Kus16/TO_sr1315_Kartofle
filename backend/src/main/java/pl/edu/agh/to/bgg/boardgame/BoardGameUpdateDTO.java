package pl.edu.agh.to.bgg.boardgame;

import jakarta.validation.constraints.Min;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Nullable;

public record BoardGameUpdateDTO(
        @Nullable
        String description,

        @Nullable
        @Min(value = 1, message = "minutesPlaytime must be at least 1")
        Integer minutesPlaytime,

        @Nullable
        MultipartFile image,

        @Nullable
        MultipartFile pdfInstruction
) {
}
