package pl.edu.agh.to.bgg.file;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.edu.agh.to.bgg.exception.FileStorageException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class FileStorageService {
    private final FileStoragePathResolver fileStoragePathResolver;

    public FileStorageService(FileStoragePathResolver fileStoragePathResolver) {
        this.fileStoragePathResolver = fileStoragePathResolver;
    }

    public Path save(MultipartFile file, StoredFileType fileType, String fileName) {
        try {
            String storageDirPath = fileStoragePathResolver.resolve(fileType);

            Path projectRoot = Path.of("").toAbsolutePath();
            Path targetDir = projectRoot.resolve(storageDirPath);
            Files.createDirectories(targetDir);

            Path filePath = targetDir.resolve(fileName);
            file.transferTo(filePath.toFile());

            return filePath;
        } catch (IOException e) {
            throw new FileStorageException("Failed to save file", e);
        }
    }

    public void deleteIfExists(StoredFile file) {
        Path path = Path.of(file.getStoragePath());
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new FileStorageException("Failed to delete file" + path, e);
        }
    }
}
