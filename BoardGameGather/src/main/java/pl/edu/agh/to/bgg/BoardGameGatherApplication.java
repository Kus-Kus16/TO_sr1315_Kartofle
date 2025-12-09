package pl.edu.agh.to.bgg;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import pl.edu.agh.to.bgg.boardgame.BoardGameRepository;

@SpringBootApplication
public class BoardGameGatherApplication {

    public static void main(String[] args) {
        SpringApplication.run(BoardGameGatherApplication.class, args);
    }

    @Configuration
    public static class BoardGameCreator {
        private final BoardGameRepository boardGameRepository;

        public BoardGameCreator(BoardGameRepository boardGameRepository) {
            this.boardGameRepository = boardGameRepository;
        }

        @PostConstruct
        public void init() {
            if (boardGameRepository.count() == 0) {

            }
        }
    }
}
