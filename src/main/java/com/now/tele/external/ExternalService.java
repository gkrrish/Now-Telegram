package com.now.tele.external;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.now.tele.configurations.MyTelegramBot;
import com.now.tele.request.WelcomeRequest;
import com.now.tele.response.WelcomeBackPDFResponse;
import com.now.tele.response.WelcomeImageResponse;
import com.now.tele.response.WelcomeResponse;
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
    	
    	WelcomeRequest welcomeRequest= new WelcomeRequest(messageText, messageText);
    	
        WelcomeResponse welcomeResponse = restTemplate.postForObject(BASE_URL, welcomeRequest, WelcomeResponse.class);
        sendResponseToUser(chatId, welcomeResponse);
    }

    private void sendResponseToUser(long chatId, WelcomeResponse welcomeResponse) throws IOException, TelegramApiException {
        if (welcomeResponse instanceof WelcomeImageResponse) {
            WelcomeImageResponse imageResponse = (WelcomeImageResponse) welcomeResponse;
            InlineKeyboardMarkup markup = ButtonUtility.createInlineKeyboard(imageResponse.getLanguages());
            SendPhoto photoMessage = TelegramMessageUtility.createPhotoMessage(chatId, "https://example.com/image.jpg", imageResponse.getMessage(), markup);
            telegramBot.execute(photoMessage);
        } else if (welcomeResponse instanceof WelcomeBackPDFResponse) {
            WelcomeBackPDFResponse pdfResponse = (WelcomeBackPDFResponse) welcomeResponse;
            InlineKeyboardMarkup markup = ButtonUtility.createInlineKeyboard(pdfResponse.getLanguages());
            SendMessage pdfMessage = TelegramMessageUtility.createTextMessage(chatId, pdfResponse.getMessage(), markup);
            telegramBot.execute(pdfMessage);
            // Additional logic to send the PDF file can be added here
        } else {
            InlineKeyboardMarkup markup = ButtonUtility.createInlineKeyboard(welcomeResponse.getLanguages());
            SendMessage textMessage = TelegramMessageUtility.createTextMessage(chatId, welcomeResponse.getMessage(), markup);
            telegramBot.execute(textMessage);
        }
    }
}
