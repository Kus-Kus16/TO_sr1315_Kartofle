package pl.edu.agh.to.bgg.boardgame.external.geek;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import pl.edu.agh.to.bgg.boardgame.BoardGame;
import pl.edu.agh.to.bgg.boardgame.external.ExternalBoardGameEntry;
import pl.edu.agh.to.bgg.boardgame.external.ExternalBoardGameProvider;
import pl.edu.agh.to.bgg.boardgame.external.geek.dto.GeekBoardGameDetailsDTO;
import pl.edu.agh.to.bgg.boardgame.external.geek.dto.GeekBoardGameEntryDTO;
import pl.edu.agh.to.bgg.exception.BoardGameNotFoundException;

import java.util.List;
import java.util.Optional;

// TODO remove after real api implemented
@Service
@Primary
public class DummyApiClient implements ExternalBoardGameProvider {

    private List<GeekBoardGameEntryDTO> dummyApiSearch() {
        return List.of(
                new GeekBoardGameEntryDTO(
                        19419,
                        "Euphrates & Tigris: Contest of Kings",
                        1997
                ),
                new GeekBoardGameEntryDTO(
                        42,
                        "Tigris & Euphrates",
                        1997
                ),
                new GeekBoardGameEntryDTO(
                        421,
                        "1830: The Game of Railroads and Robber Barons",
                        1986
                )
        );
    }

    private Optional<GeekBoardGameDetailsDTO> dummyApiGet(int id) {
        if (id != 421) {
            return Optional.empty();
        }

        return Optional.of(new GeekBoardGameDetailsDTO(
                421,
                1986,
                2,
                7,
                360,
                14,
                "1830: The Game of Railroads and Robber Barons",
                "1830 is one of the most famous 18xx games. One of the things some gamers like about this game is that the game has no 'chance' element. That is to say, if players wished to play two games with the same moves, the outcome would be the same also.&lt;br/&gt;&lt;br/&gt;This game takes the basic mechanics from Tresham's 1829, and adds several new elements. Players are seeking to make the most money by buying and selling stock in various share companies located on eastern United States map. The stock manipulation aspect of the game is widely-regarded as one of the best. The board itself is actually a fairly abstract hexagonal system, with track tiles placed on top of the hexes. Plus each 18xx title adds new and different elements to the game. This game features private rail companies and an extremely vicious, 'robber baron' oriented stock market. A game is finished when the bank runs out of money or one player is forced to declare bankruptcy, and the player with the greatest personal holdings wins.&lt;br/&gt;&lt;br/&gt;The 2011 version of 1830 was published by Mayfair Games in partnership with Lookout Games of Germany. This publication was developed under license from Francis Tresham in co-operation with Bruce Shelley (the original 1830 developer). This version contains rules and components for Francis Tresham's original classic design, a faster-playing basic game, and new variants from some of the world's best railroad game developers.",
                "https://cf.geekdo-images.com/3EmD1SEI5fVpR4rbdkU0AA__thumb/img/v_GuoUgKbMTvzcRkTcH6aOH9wWs=/fit-in/200x150/filters:strip_icc()/pic882119.jpg",
                "https://cf.geekdo-images.com/3EmD1SEI5fVpR4rbdkU0AA__original/img/M6qiL7ZNU_mjgZXHZjOrVxNamfU=/0x0/filters:format(jpeg)/pic882119.jpg"
        ));
    }

    @Override
    public List<ExternalBoardGameEntry> searchFor(String query) {
        return dummyApiSearch().stream()
                .map(GeekBoardGameEntryDTO::toExternalBoardGameEntry)
                .toList();
    }

    @Override
    public BoardGame getById(int id) {
        return dummyApiGet(id)
                .map(GeekBoardGameDetailsDTO::toBoardGame)
                .orElseThrow(BoardGameNotFoundException::new);
    }

}
