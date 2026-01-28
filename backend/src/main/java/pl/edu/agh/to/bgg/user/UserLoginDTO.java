package pl.edu.agh.to.bgg.user;

import jakarta.validation.constraints.NotBlank;

public record UserLoginDTO(
        @NotBlank(message = "Username must not be blank")
        String username
){
}
