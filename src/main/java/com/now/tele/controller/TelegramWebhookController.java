package com.now.tele.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.now.tele.configurations.MyTelegramBot;
import com.now.tele.external.ExternalService;

@RestController
public class TelegramWebhookController {
	
	@Autowired
	private ExternalService externalService;

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
            String responseText = processUserMessage(messageText, chatId);
            
         // Send an image to the user
            sendPhoto(chatId, "https://example.com/image.jpg", "Here is an image for you!");


            // Send a response back to the user
            SendMessage message = createInlineKeyboardMessage(chatId, responseText);

            try {
                telegramBot.execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        } else if (update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();
            long chatId = update.getCallbackQuery().getMessage().getChatId();

            // Process callback query data
            String responseText = processCallbackQuery(callbackData);

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

    private String processUserMessage(String messageText, long chatId) {
    	
    	
        return "Processed message: " + messageText;
    }

    private String processCallbackQuery(String callbackData) {
        // Process the callback data and return a response message
        return "You pressed: " + callbackData;
    }

    private SendMessage createInlineKeyboardMessage(long chatId, String text) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();

        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setText("Button 1");
        button1.setCallbackData("button1");

        InlineKeyboardButton button2 = new InlineKeyboardButton();
        button2.setText("Button 2");
        button2.setCallbackData("button2");

        row1.add(button1);
        row1.add(button2);

        rows.add(row1);

        inlineKeyboardMarkup.setKeyboard(rows);

        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        message.setReplyMarkup(inlineKeyboardMarkup);

        return message;
    }
    
    private void sendPhoto(long chatId, String photoUrl, String caption) {
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(String.valueOf(chatId));
        sendPhoto.setPhoto(new InputFile(photoUrl));
        sendPhoto.setCaption(caption);

        try {
            telegramBot.execute(sendPhoto);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
