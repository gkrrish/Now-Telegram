package com.now.tele.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class WelcomeBackPDFResponse extends WelcomeResponse {
    private byte[] invoice;
    private String delta;

    public WelcomeBackPDFResponse(String message) {
        super(message);
    }
}
