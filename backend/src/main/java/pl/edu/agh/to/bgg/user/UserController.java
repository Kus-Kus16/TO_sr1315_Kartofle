package pl.edu.agh.to.bgg.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("users")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("register")
    public User registerUser(@RequestBody LoginDTO dto) {
        try {
            return userService.addUser(dto.username());
        } catch (UsernameAlreadyExistsException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("login")
    public User loginUser(@RequestBody LoginDTO dto) {
        try {
            return userService.getUser(dto.username());
        } catch (UserNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    public record LoginDTO(String username) {
    }
}
