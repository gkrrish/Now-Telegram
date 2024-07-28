package com.now.tele.response;

import lombok.Data;

import java.util.List;

@Data
public class WelcomeResponse {
    private String message;
    private List<String> languages;

    public WelcomeResponse(String message) {
        this.message = message;
    }
}
