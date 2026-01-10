package pl.edu.agh.to.bgg.boardgame.external.geek;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import pl.edu.agh.to.bgg.boardgame.BoardGame;
import pl.edu.agh.to.bgg.boardgame.external.ExternalBoardGameEntry;
import pl.edu.agh.to.bgg.boardgame.external.ExternalBoardGameProvider;
import pl.edu.agh.to.bgg.boardgame.external.geek.dto.GeekBoardGameDetailsDTO;
import pl.edu.agh.to.bgg.boardgame.external.geek.dto.GeekBoardGameEntryDTO;

import java.util.List;

@Service
public class GeekApiClient implements ExternalBoardGameProvider {
    private static class GeekBoardGameEntryDTOList {
        @JacksonXmlElementWrapper(useWrapping = false)
        @JacksonXmlProperty(localName = "boardgame") // todo ??
        private List<GeekBoardGameEntryDTO> boardgames;

        public List<GeekBoardGameEntryDTO> getBoardgames() {
            return boardgames;
        }

        public void setBoardgames(List<GeekBoardGameEntryDTO> boardgames) {
            this.boardgames = boardgames;
        }
    }

    private final static String URL = "https://boardgamegeek.com/xmlapi2/";
    private final RestClient restClient = RestClient.create();

    @Override
    public List<ExternalBoardGameEntry> searchFor(String query) {
        GeekBoardGameEntryDTOList response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(URL)
                        .path("search")
                        .queryParam("query", query)
                        .queryParam("type", "boardgame")
                        .build())
                .accept(MediaType.APPLICATION_XML)
                .retrieve()
                .body(GeekBoardGameEntryDTOList.class);

        return response.getBoardgames()
                .stream()
                .map(GeekBoardGameEntryDTO::toExternalBoardGameEntry)
                .toList();
    }

    @Override
    public BoardGame getById(int id) {
        GeekBoardGameDetailsDTO response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(URL)
                        .path("thing")
                        .queryParam("id", id)
                        .queryParam("type", "boardgame")
                        .build())
                .accept(MediaType.APPLICATION_XML)
                .retrieve()
                .body(GeekBoardGameDetailsDTO.class);

        return response.toBoardGame();
    }
}


