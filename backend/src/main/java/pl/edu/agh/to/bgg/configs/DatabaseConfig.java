package pl.edu.agh.to.bgg.configs;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import pl.edu.agh.to.bgg.boardgame.BoardGame;
import pl.edu.agh.to.bgg.boardgame.BoardGameRepository;
import pl.edu.agh.to.bgg.file.FileStoragePathResolver;
import pl.edu.agh.to.bgg.file.StoredFileType;
import pl.edu.agh.to.bgg.session.GameSession;
import pl.edu.agh.to.bgg.session.GameSessionRepository;
import pl.edu.agh.to.bgg.user.User;
import pl.edu.agh.to.bgg.user.UserRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

@Configuration
@Profile("dev")
public class DatabaseConfig {
    private final BoardGameRepository boardGameRepository;
    private final UserRepository userRepository;
    private final GameSessionRepository gameSessionRepository;
    private final FileStoragePathResolver fileStoragePathResolver;

    public DatabaseConfig(BoardGameRepository boardGameRepository, UserRepository userRepository,
                          GameSessionRepository gameSessionRepository, FileStoragePathResolver fileStoragePathResolver) {
        this.boardGameRepository = boardGameRepository;
        this.userRepository = userRepository;
        this.gameSessionRepository = gameSessionRepository;
        this.fileStoragePathResolver = fileStoragePathResolver;
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
        bg1.setDiscontinued(true);

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
                "Rozgrywka z dziekanem",
                LocalDate.now().plusDays(1),
                4,
                "Niepowtarzalna okazja",
                Set.of(bg1,bg2),
                u1
        );
        s1.getParticipants().add(u1);
        s1.getParticipants().add(u2);
        s1.getParticipants().add(u3);

        GameSession s2 = new GameSession(
                "Dwuosobowa kooperacja",
                LocalDate.now().plusWeeks(1),
                2,
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse sit amet mi est. Nunc tristique faucibus sapien, eleifend mattis augue euismod nec. Etiam accumsan et purus id fermentum. Fusce pellentesque nibh at orci lacinia semper. Cras nec risus ut lacus tempus ullamcorper. Nulla consequat odio eget neque interdum iaculis. Duis nisi diam, condimentum in lacus nec, condimentum sodales risus. Donec iaculis odio eu orci bibendum, eu luctus justo blandit.",
                Set.of(bg2),
                u2
        );
        s2.getParticipants().add(u2);
        s2.setSelectedBoardGame(bg2);

        GameSession s3 = new GameSession(
                "Stara sesja",
                LocalDate.now().minusMonths(1),
                2,
                "Bardzo stara",
                Set.of(bg2),
                u2
        );
        s3.getParticipants().add(u2);
        s2.setSelectedBoardGame(bg2);

        GameSession s4 = new GameSession(
                "Pełna sesja",
                LocalDate.now().plusWeeks(1),
                1,
                "Solowa rozgrywka",
                Set.of(bg2),
                u3
        );
        s4.getParticipants().add(u3);
        s2.setSelectedBoardGame(bg2);

        boardGameRepository.saveAll(List.of(bg1, bg2));
        userRepository.saveAll(List.of(u1, u2, u3));
        gameSessionRepository.saveAll(List.of(s1, s2, s3, s4));

        for (int i = 0; i < 10; i++) {
            BoardGame bg = new BoardGame(
                    "Lorem " + i,
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse sit amet mi est. Nunc tristique faucibus sapien, eleifend mattis augue euismod nec. Etiam accumsan et purus id fermentum. Fusce pellentesque nibh at orci lacinia semper. Cras nec risus ut lacus tempus ullamcorper. Nulla consequat odio eget neque interdum iaculis. Duis nisi diam, condimentum in lacus nec, condimentum sodales risus. Donec iaculis odio eu orci bibendum, eu luctus justo blandit.",
                    1,
                    4,
                    90
            );
            boardGameRepository.save(bg);
        }
    }

    @PreDestroy
    public void cleanup() {
        for (StoredFileType type : StoredFileType.values()) {
            Path storagePath = Path.of(fileStoragePathResolver.resolve(type));
            if (Files.exists(storagePath)) {
                try (Stream<Path> paths = Files.walk(storagePath)){
                    paths
                        .sorted(Comparator.reverseOrder())
                        .forEach(p -> {
                            try {
                                Files.deleteIfExists(p);
                            } catch (IOException e) {
                                System.err.println("Nie udało się usunąć pliku: " + p);
                            }
                        });
                } catch (IOException e) {
                    System.err.println("Błąd podczas usuwania folderu: " + storagePath);
                }
            }
        }
    }
}
