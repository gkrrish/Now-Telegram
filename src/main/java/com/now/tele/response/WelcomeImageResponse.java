package com.now.tele.response;


import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

import org.springframework.core.io.ClassPathResource;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class WelcomeImageResponse extends WelcomeResponse {
    private byte[] imageData;
    private String imageBase64;

    public WelcomeImageResponse(String message) throws IOException {
        super(message);
        this.loadImageBytes();
        this.setImageBase64();
    }

    private void loadImageBytes() {
        try {
            ClassPathResource resource = new ClassPathResource("images/welcome-banner.PNG");
            this.imageData = Files.readAllBytes(resource.getFile().toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This is is using for the, " some time our controller returns the PDF /Image so getting response.body is not at possible so that both model 
     * classes converted to base64 so that the headers and body content we can able to fetch.
     */
    private void setImageBase64() { 
        this.imageBase64 = Base64.getEncoder().encodeToString(this.imageData);
    }
}
