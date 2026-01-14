package pl.edu.agh.to.bgg.exception;

public class VoteNotFoundException extends NotFoundException {

    public VoteNotFoundException() {
        super("Vote not found");
    }
}
