package pl.edu.agh.to.bgg.file;

import jakarta.transaction.Transactional;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.edu.agh.to.bgg.exception.StoredFileNotFoundException;

import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

@Service
public class StoredFileService {
    private static final long IMAGE_MAX_SIZE = 2 * 1024 * 1024; // 2 MB
    private static final long PDF_MAX_SIZE = 5 * 1024 * 1024; // 5 MB

    private final StoredFileRepository storedFileRepository;
    private final FileStorageService fileStorageService;

    public StoredFileService(StoredFileRepository storedFileRepository, FileStorageService fileStorageService) {
        this.storedFileRepository = storedFileRepository;
        this.fileStorageService = fileStorageService;
    }

    @Transactional
    public StoredFile saveFile(MultipartFile file) {
        try {
            return saveFile(file.getOriginalFilename(), file.getContentType(), file.getBytes(), file.getSize());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public StoredFile saveFile(String filename, String contentType, byte[] content, long size) {
        if (contentType == null) {
            throw new IllegalArgumentException("StoredFile content type is null");
        }

        StoredFileType fileType = switch (contentType) {
            case String ct when ct.startsWith("image/") -> {
                validateImage(size);
                yield StoredFileType.IMAGE;
            }
            case String ct when ct.startsWith("application/pdf") -> {
                validatePdfFile(size);
                yield StoredFileType.PDF;
            }
            default -> throw new IllegalArgumentException(
                "StoredFile should be image or pdf"
            );
        };

        UUID id = UUID.randomUUID();
        StoredFile storedFile = new StoredFile(
                id,
                filename,
                contentType,
                size
        );

        Path filepath = fileStorageService.save(content, fileType, id.toString());
        storedFile.setStoragePath(filepath.toString());

        return storedFileRepository.save(storedFile);
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

        fileStorageService.deleteIfExists(storedFile);
        storedFileRepository.delete(storedFile);
    }

    private void validateImage(long size) {
        if (size > IMAGE_MAX_SIZE) {
            throw new IllegalArgumentException("Image size should be no more than 2MB");
        }
    }

    private void validatePdfFile(long size) {
        if (size > PDF_MAX_SIZE) {
            throw new IllegalArgumentException("PDF size should be no more than 5MB");
        }
    }
}
