package com.now.tele.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.now.tele.configurations.MyTelegramBot;

@RestController
public class TelegramWebhookController {

    private final MyTelegramBot telegramBot;

    public TelegramWebhookController(MyTelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    @PostMapping("/webhook")
    public void onUpdateReceived(@RequestBody Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            // Process the message and perform necessary operations
            String responseText = processUserMessage(messageText);

            // Send a response back to the user
            SendMessage message = new SendMessage();
            message.setChatId(String.valueOf(chatId));
            message.setText(responseText);

            try {
                telegramBot.execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private String processUserMessage(String messageText) {
        // Perform your operations here, e.g., checking order status, placing a new order, etc.
        return "Processed message: " + messageText;
    }
}
