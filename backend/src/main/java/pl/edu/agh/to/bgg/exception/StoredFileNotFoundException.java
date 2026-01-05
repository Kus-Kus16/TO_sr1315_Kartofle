package pl.edu.agh.to.bgg.exception;

public class StoredFileNotFoundException extends NotFoundException {

    public StoredFileNotFoundException() {
        super("Stored file not found");
    }
}
