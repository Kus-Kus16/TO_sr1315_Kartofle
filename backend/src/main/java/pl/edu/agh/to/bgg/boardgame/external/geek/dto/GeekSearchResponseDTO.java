package pl.edu.agh.to.bgg.boardgame.external.geek.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.List;

@JacksonXmlRootElement(localName = "items")
public record GeekSearchResponseDTO(
        @JacksonXmlElementWrapper(useWrapping = false)
        @JacksonXmlProperty(localName = "item")
        List<SearchItem> searchItems
) {
    public boolean isEmpty() {
        return searchItems == null || searchItems.isEmpty();
    }

    public record SearchItem(
            @JacksonXmlProperty(isAttribute = true)
            int id,

            Name name,
            YearPublished yearpublished
    ) {}

    public record Name(
            @JacksonXmlProperty(isAttribute = true)
            String value
    ) {}

    public record YearPublished(
            @JacksonXmlProperty(isAttribute = true)
            int value
    ) {}
}

