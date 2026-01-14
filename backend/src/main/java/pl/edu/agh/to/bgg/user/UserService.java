package pl.edu.agh.to.bgg.user;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import pl.edu.agh.to.bgg.exception.UserNotFoundException;
import pl.edu.agh.to.bgg.exception.UsernameAlreadyExistsException;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User addUser(String username) {
        try {
            User user = new User(username);
            return userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new UsernameAlreadyExistsException();
        }
    }

    public User getUser(String username) {
        return userRepository
                .findByUsername(username)
                .orElseThrow(UserNotFoundException::new);
    }
}
