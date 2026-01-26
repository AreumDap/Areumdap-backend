package com.umc9th.areumdap.domain.character.resolver;

import com.umc9th.areumdap.domain.character.enums.CharacterSeason;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CharacterImageResolver {

    @Value("${areumdap.character.image.base-url}")
    private String baseUrl;

    public String resolve(CharacterSeason characterSeason, int level) {
        return baseUrl +
                "/character/" +
                characterSeason.name().toLowerCase() +
                "/stage" + level + ".png";
    }

}
