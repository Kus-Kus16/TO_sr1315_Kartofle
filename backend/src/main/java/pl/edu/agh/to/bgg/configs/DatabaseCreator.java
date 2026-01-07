package pl.edu.agh.to.bgg.configs;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import pl.edu.agh.to.bgg.boardgame.BoardGame;
import pl.edu.agh.to.bgg.boardgame.BoardGameRepository;
import pl.edu.agh.to.bgg.session.GameSession;
import pl.edu.agh.to.bgg.session.GameSessionRepository;
import pl.edu.agh.to.bgg.user.User;
import pl.edu.agh.to.bgg.user.UserRepository;

import java.time.LocalDate;
import java.util.List;

@Configuration
@Profile("dev")
public class DatabaseCreator {
    private final BoardGameRepository boardGameRepository;
    private final UserRepository userRepository;
    private final GameSessionRepository gameSessionRepository;

    public DatabaseCreator(BoardGameRepository boardGameRepository, UserRepository userRepository, GameSessionRepository gameSessionRepository) {
        this.boardGameRepository = boardGameRepository;
        this.userRepository = userRepository;
        this.gameSessionRepository = gameSessionRepository;
    }

    @PostConstruct
    public void init() {
        BoardGame bg1 = new BoardGame(
                "7 cudów świata",
                "Ciekawa gra",
                2,
                7,
                30
        );

        BoardGame bg2 = new BoardGame(
                "Robinson Crusoe",
                "Ciekawa gra",
                1,
                4,
                90
        );
        
        User u1 = new User("mag");
        User u2 = new User("marcin");
        User u3 = new User("kmicic");

        GameSession s1 = new GameSession(
                LocalDate.now().plusDays(1),
                4,
                "Rozgrywka z dziekanem",
                bg1,
                u1
        );
        s1.getParticipants().add(u1);
        s1.getParticipants().add(u2);
        s1.getParticipants().add(u3);

        GameSession s2 = new GameSession(
                LocalDate.now().plusWeeks(1),
                2,
                "Dwuosobowa kooperacja",
                bg2,
                u2
        );
        s2.getParticipants().add(u2);

        boardGameRepository.saveAll(List.of(bg1, bg2));
        userRepository.saveAll(List.of(u1, u2, u3));
        gameSessionRepository.saveAll(List.of(s1, s2));
    }
}
