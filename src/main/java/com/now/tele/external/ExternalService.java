package com.now.tele.external;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.now.tele.configurations.MyTelegramBot;
import com.now.tele.request.WelcomeRequest;
import com.now.tele.util.ButtonUtility;
import com.now.tele.util.TelegramMessageUtility;

@Service
public class ExternalService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private MyTelegramBot telegramBot;

    private static final String BASE_URL = "http://localhost:9009/welcome";

    public void handleInitialRequest(String messageText, long chatId) throws IOException, TelegramApiException {
    	WelcomeRequest welcomeRequest = new WelcomeRequest();
    	welcomeRequest.setMobileNumber(Long.toString(chatId));
    	welcomeRequest.setWelcomeAnyMessage(messageText);
    	
        ResponseEntity<byte[]> response = restTemplate.postForEntity(BASE_URL, welcomeRequest, byte[].class);
        String contentType = response.getHeaders().getContentType().toString();

        if (contentType.equals("image/png") || contentType.equals("image/jpeg")) {
            sendImageResponse(chatId, response.getBody());
        } else if (contentType.equals("application/pdf")) {
            sendPdfResponse(chatId, response.getBody());
        } else {
            sendTextResponse(chatId, new String(response.getBody()));
        }
    }

    private void sendPdfResponse(long chatId, byte[] pdfData) throws TelegramApiException {
    	InlineKeyboardMarkup markup = ButtonUtility.createInlineKeyboard(List.of("Option 1", "Option 2")); // Example buttons
        InputFile inputFile = new InputFile(new ByteArrayInputStream(pdfData), "document.pdf");
        SendDocument documentMessage = TelegramMessageUtility.createDocumentMessage(chatId, inputFile, "Here is your document", markup);
        telegramBot.execute(documentMessage);
    }

    private void sendTextResponse(long chatId, String messageText) throws TelegramApiException {
        InlineKeyboardMarkup markup = ButtonUtility.createInlineKeyboard(List.of("Option 1", "Option 2")); // Example buttons
        SendMessage textMessage = TelegramMessageUtility.createTextMessage(chatId, messageText, markup);
        telegramBot.execute(textMessage);
    }
    
    private void sendImageResponse(long chatId, byte[] imageData) throws TelegramApiException {
        InlineKeyboardMarkup markup = ButtonUtility.createInlineKeyboard(List.of("Option 1", "Option 2")); // Example buttons

        // Create an InputFile from the byte array
        InputFile inputFile = new InputFile(new ByteArrayInputStream(imageData), "image.png");

        // Use the InputFile in the SendPhoto method
        SendPhoto photoMessage = TelegramMessageUtility.createPhotoMessage(chatId, inputFile, "Here is your image", markup);
        telegramBot.execute(photoMessage);
    }
}
