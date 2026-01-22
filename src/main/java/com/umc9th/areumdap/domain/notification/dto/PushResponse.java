package com.umc9th.areumdap.domain.notification.dto;

class Notification {
    private String title;
    private String body;
}

public class PushResponse {
    private Notification notification;
    private Object Data;
    private String To;
}