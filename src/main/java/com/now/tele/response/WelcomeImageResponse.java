package com.now.tele.response;

import java.io.IOException;
import java.nio.file.Files;

import org.springframework.core.io.ClassPathResource;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class WelcomeImageResponse extends WelcomeResponse {
    private byte[] imageData;

    public WelcomeImageResponse(String message) {
        super(message);
        this.loadImageBytes();
    }

    private void loadImageBytes() {
        try {
            ClassPathResource resource = new ClassPathResource("images/welcome-banner.PNG");
            this.imageData = Files.readAllBytes(resource.getFile().toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
