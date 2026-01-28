package pl.edu.agh.to.bgg.boardgame.external.geek;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.edu.agh.to.bgg.boardgame.external.geek.dto.GeekSearchResponseDTO;
import pl.edu.agh.to.bgg.boardgame.external.geek.dto.GeekThingResponseDTO;

import java.util.List;
import java.util.Optional;

@Service
public class GeekApiClient {
    private final String geekToken;
    private final GeekFeignClient geekFeignClient;

    public GeekApiClient(@Value("${app.external-token}") String geekToken, GeekFeignClient geekFeignClient) {
        this.geekToken = geekToken;
        this.geekFeignClient = geekFeignClient;
    }

    public GeekSearchResponseDTO searchFor(String query) {
        GeekSearchResponseDTO response = geekFeignClient.search(
                query,
                "boardgame",
                "Bearer " + geekToken
        );

        if (response == null || response.isEmpty()) {
            return new GeekSearchResponseDTO(List.of());
        }

        return response;
    }

    public Optional<GeekThingResponseDTO.ItemDetails> getById(int id) {
        GeekThingResponseDTO response = geekFeignClient.thing(
                id,
                "boardgame",
                "Bearer " + geekToken
        );

        if (response == null || response.isEmpty()) {
            return Optional.empty();
        }

        return Optional.ofNullable(response.getItem());
    }
}


