package com.umc9th.areumdap.common.jwt.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JwtTokenType {
    ACCESS("access"),
    REFRESH("refresh");

    private final String type;
}
