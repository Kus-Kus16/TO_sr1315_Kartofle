package pl.edu.agh.to.bgg.boardgame.external.geek.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.List;
import java.util.Objects;

@JacksonXmlRootElement(localName = "items")
public record GeekThingResponseDTO (

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "item")
    List<ItemDetails> itemDetails
) {
    public boolean isEmpty() {
        return itemDetails == null || itemDetails.isEmpty() || itemDetails.getFirst() == null;
    }
    public ItemDetails getItem() { return itemDetails.getFirst(); }

    public record ItemDetails(
            String thumbnail,
            String image,

            @JacksonXmlElementWrapper(useWrapping = false)
            @JacksonXmlProperty(localName = "name")
            List<Name> names,

            String description,
            YearPublished yearpublished,
            MinPlayers minplayers,
            MaxPlayers maxplayers,
            PlayingTime playingtime
    ) {
        public Name getPrimaryName() {
            return names.stream()
                    .filter(name -> Objects.equals(name.type, "primary"))
                    .findFirst()
                    .orElse(names.getFirst());
        }
    }

    public record Name(
            @JacksonXmlProperty(isAttribute = true)
            String type,

            @JacksonXmlProperty(isAttribute = true)
            String value
    ) {}

    public record YearPublished(
            @JacksonXmlProperty(isAttribute = true)
            int value
    ) {}

    public record MinPlayers(
            @JacksonXmlProperty(isAttribute = true)
            int value
    ) {}

    public record MaxPlayers(
            @JacksonXmlProperty(isAttribute = true)
            int value
    ) {}

    public record PlayingTime(
            @JacksonXmlProperty(isAttribute = true)
            int value
    ) {}
}
