package pl.edu.agh.to.bgg.file;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = StoredFile.TABLE_NAME)
public class StoredFile {
    public static final String TABLE_NAME = "files";
    public static class Columns {
        public static final String ID = "id";
        public static final String ORIGINAL_NAME = "original_name";
        public static final String CONTENT_TYPE = "content_type";
        public static final String SIZE = "size";
        public static final String STORAGE_PATH = "storage_path";
    }

    @Id
    @Column(name = Columns.ID)
    private UUID id;

    @Column(name = Columns.ORIGINAL_NAME, nullable = false)
    private String originalName;

    @Column(name = Columns.CONTENT_TYPE, nullable = false)
    private String contentType;

    @Column(name = Columns.SIZE, nullable = false)
    private long size;

    @Column(name = Columns.STORAGE_PATH, nullable = false)
    private String storagePath;

    public StoredFile(UUID id, String originalName, String contentType, long size) {
        this.id = id;
        this.originalName = originalName;
        this.contentType = contentType;
        this.size = size;
    }

    public StoredFile() {

    }

    public UUID getId() {
        return id;
    }

    public String getOriginalName() {
        return originalName;
    }

    public String getContentType() {
        return contentType;
    }

    public long getSize() {
        return size;
    }

    public String getStoragePath() {
        return storagePath;
    }

    public void setStoragePath(String storagePath) {
        this.storagePath = storagePath;
    }
}
