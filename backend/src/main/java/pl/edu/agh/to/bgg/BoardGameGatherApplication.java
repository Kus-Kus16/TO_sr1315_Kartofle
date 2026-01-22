package pl.edu.agh.to.bgg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class BoardGameGatherApplication {

    public static void main(String[] args) {
        SpringApplication.run(BoardGameGatherApplication.class, args);
    }
}
