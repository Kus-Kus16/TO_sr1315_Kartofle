package pl.edu.agh.to.bgg.exception;

public class GameSessionNotFoundException extends NotFoundException {

    public GameSessionNotFoundException() {
        super("Game session not found");
    }
}
