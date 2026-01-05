package pl.edu.agh.to.bgg.file;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.edu.agh.to.bgg.exception.StoredFileNotFoundException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Service
public class StoredFileService {
    @Value("${app.image-storage-path}")
    private String imageStoragePath;

    @Value("${app.pdf-storage-path}")
    private String pdfStoragePath;

    private static final long IMAGE_MAX_SIZE = 2 * 1024 * 1024; // 2 MB
    private static final long PDF_MAX_SIZE = 5 * 1024 * 1024; // 5 MB

    private final StoredFileRepository storedFileRepository;

    public StoredFileService(StoredFileRepository storedFileRepository) {
        this.storedFileRepository = storedFileRepository;
    }

    @Transactional
    public StoredFile saveFile(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null) {
            throw new IllegalArgumentException("StoredFile content type is null");
        }

        String storageDirPath;
        if (contentType.startsWith("image/")) {
            storageDirPath = imageStoragePath;
            validateImage(file);
        } else if (contentType.startsWith("application/pdf")) {
            storageDirPath = pdfStoragePath;
            validatePdfFile(file);
        } else {
            throw new IllegalArgumentException("StoredFile should be image or pdf");
        }

        try {
            UUID id = UUID.randomUUID();
            StoredFile storedFile = new StoredFile(
                    id,
                    file.getOriginalFilename(),
                    contentType,
                    file.getSize());

            Path projectRoot = Path.of("").toAbsolutePath();
            Path targetDir = projectRoot.resolve(storageDirPath);
            Files.createDirectories(targetDir);

            String fileName = id.toString();
            Path filePath = targetDir.resolve(fileName);
            file.transferTo(filePath.toFile());

            storedFile.setStoragePath(filePath.toString());
            return storedFileRepository.save(storedFile);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save file", e);
        }
    }

    public StoredFile getFile(UUID id) {
        return storedFileRepository
                .findById(id)
                .orElseThrow(StoredFileNotFoundException::new);
    }

    @Transactional
    public Resource loadFile(UUID id) {
        StoredFile file = storedFileRepository
                .findById(id)
                .orElseThrow(StoredFileNotFoundException::new);

        Path path = Path.of(file.getStoragePath());
        return new FileSystemResource(path.toFile());
    }

    @Transactional
    public void deleteFile(StoredFile storedFile) {
        if (storedFile == null) {
            return;
        }

        Path path = Path.of(storedFile.getStoragePath());
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file" + path, e);
        }

        storedFileRepository.delete(storedFile);
    }

    private void validateImage(MultipartFile image) {
        if (image.getSize() > IMAGE_MAX_SIZE) {
            throw new IllegalArgumentException("Image size should be no more than 2MB");
        }
    }

    private void validatePdfFile(MultipartFile pdfFile) {
        if (pdfFile.getSize() > PDF_MAX_SIZE) {
            throw new IllegalArgumentException("PDF size should be no more than 5MB");
        }
    }
}
