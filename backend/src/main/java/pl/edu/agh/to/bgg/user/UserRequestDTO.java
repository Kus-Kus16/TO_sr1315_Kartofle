package pl.edu.agh.to.bgg.user;

import jakarta.validation.constraints.NotBlank;

public record UserRequestDTO (
    @NotBlank(message = "username cannot be blank")
    String username){
}
