package com.umc9th.areumdap.domain.character.dto.response;

import java.util.List;
import lombok.Builder;

@Builder
public record CharacterMainResponse(
        Long characterId,
        String name,
        Integer level,
        Integer currentXp,
        Integer goalXp,
        Boolean hasLevelUpParams,
        List<CharacterQuestDto> quests
) {
}
