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
}
