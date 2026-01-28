package pl.edu.agh.to.bgg.user;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User registerUser(@RequestBody @Valid UserLoginDTO dto) {
        return userService.addUser(dto.username());
    }

    @PostMapping("login")
    public User loginUser(@RequestBody @Valid UserLoginDTO dto) {
        return userService.getUser(dto.username());
    }
}
