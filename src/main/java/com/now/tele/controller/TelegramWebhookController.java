package com.now.tele.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.now.tele.external.ExternalService;

@RestController
public class TelegramWebhookController {

    @Autowired
    private ExternalService externalService;

    @PostMapping("/webhook")
    public void onUpdateReceived(@RequestBody Update update) {
        try {
            if (update.hasMessage() && update.getMessage().hasText()) {
                String messageText = update.getMessage().getText();
                long chatId = update.getMessage().getChatId();
                externalService.handleInitialRequest(messageText, chatId);
            } else if (update.hasCallbackQuery()) {
                String callbackData = update.getCallbackQuery().getData();
                long chatId = update.getCallbackQuery().getMessage().getChatId();
                // Handle callback query here
            }
        } catch (IOException | TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
