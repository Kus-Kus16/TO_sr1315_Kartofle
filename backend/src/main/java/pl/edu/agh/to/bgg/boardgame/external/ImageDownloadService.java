package pl.edu.agh.to.bgg.boardgame.external;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.net.URI;

@Service
public class ImageDownloadService {
    private final RestClient restClient;

    public ImageDownloadService() {
        restClient = RestClient.create();
    }

    public byte[] download(String imageUrl) {
        return restClient.get()
                .uri(URI.create(imageUrl))
                .accept(MediaType.APPLICATION_OCTET_STREAM)
                .retrieve()
                .body(byte[].class);
    }
}
