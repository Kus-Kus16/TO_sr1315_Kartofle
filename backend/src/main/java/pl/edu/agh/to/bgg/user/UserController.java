package pl.edu.agh.to.bgg.user;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User registerUser(@RequestBody @Valid UserRequestDTO dto) {
        try {
            return userService.addUser(dto.username());
        } catch (UsernameAlreadyExistsException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
        }
    }

    @PostMapping("login")
    public User loginUser(@RequestBody @Valid UserRequestDTO dto) {
        try {
            return userService.getUser(dto.username());
        } catch (UserNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
