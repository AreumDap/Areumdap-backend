package com.umc9th.areumdap.domain.push.dto;

import lombok.Getter;

import java.util.Map;

@Getter
public class PushRequest {
    private String token;
    private String title;
    private String body;
    private Map<String, String> data;
}
