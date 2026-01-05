package pl.edu.agh.to.bgg.file;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("files")
public class StoredFileController {
    private final StoredFileService storedFileService;

    public StoredFileController(StoredFileService storedFileService) {
        this.storedFileService = storedFileService;
    }

    @GetMapping("{id}")
    public ResponseEntity<Resource> getFile(@PathVariable("id") UUID fileId) {
        StoredFile file = storedFileService.getFile(fileId);
        Resource resource = storedFileService.loadFile(fileId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + file.getOriginalName() + "\"")
                .body(resource);
    }
}
