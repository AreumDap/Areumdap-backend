package com.umc9th.areumdap.domain.character.resolver;

import com.umc9th.areumdap.domain.character.enums.Season;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CharacterImageResolver {

    @Value("${areumdap.character.image.base-url}")
    private String baseUrl;

    public String resolve(Season season, int level) {
        return baseUrl +
                "/character/" +
                season.name().toLowerCase() +
                "/stage" + level + ".png";
    }

}
