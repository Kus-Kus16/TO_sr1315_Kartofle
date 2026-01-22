package pl.edu.agh.to.bgg.boardgame.external.geek;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import pl.edu.agh.to.bgg.boardgame.external.geek.dto.GeekSearchResponseDTO;
import pl.edu.agh.to.bgg.boardgame.external.geek.dto.GeekThingResponseDTO;

@FeignClient(name = "GeekFeignClient", url = "${app.external-url}")
public interface GeekFeignClient {

    @GetMapping("/search")
    GeekSearchResponseDTO search(@RequestParam("query") String query, @RequestParam String type,
                                 @RequestHeader(HttpHeaders.AUTHORIZATION) String auth);

    @GetMapping("thing")
    GeekThingResponseDTO thing(@RequestParam("id") int id, @RequestParam("type") String type,
                               @RequestHeader(HttpHeaders.AUTHORIZATION) String auth);
}
