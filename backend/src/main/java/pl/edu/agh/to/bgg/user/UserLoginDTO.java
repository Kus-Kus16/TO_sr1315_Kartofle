package pl.edu.agh.to.bgg.user;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record UserLoginDTO(
        @NotBlank(message = "Username must not be blank")
        String username
){
}
