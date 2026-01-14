package pl.edu.agh.to.bgg.file;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class FileStoragePathResolver {

    private final Map<StoredFileType, String> paths;

    public FileStoragePathResolver(
        @Value("${app.image-storage-path}") String imagePath,
        @Value("${app.pdf-storage-path}") String pdfPath
    )
    {
        this.paths = Map.of(
                StoredFileType.IMAGE, imagePath,
                StoredFileType.PDF, pdfPath
        );
    }

    public String resolve(StoredFileType type) {
        return paths.get(type);
    }
}