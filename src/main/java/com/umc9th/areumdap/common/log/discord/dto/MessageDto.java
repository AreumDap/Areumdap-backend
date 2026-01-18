package com.umc9th.areumdap.common.log.discord.dto;

import java.util.List;

public record MessageDto(
        String content,
        List<EmbedDto> embedList
) {
}
