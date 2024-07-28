package com.now.tele.util;

import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

public class TelegramMessageUtility {

    public static SendMessage createTextMessage(long chatId, String text, InlineKeyboardMarkup markup) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        message.setReplyMarkup(markup);
        return message;
    }

    public static SendPhoto createPhotoMessage(long chatId, InputFile inputFile, String caption, InlineKeyboardMarkup markup) {
        SendPhoto photoMessage = new SendPhoto();
        photoMessage.setChatId(String.valueOf(chatId));
        photoMessage.setPhoto(inputFile);
        photoMessage.setCaption(caption);
        photoMessage.setReplyMarkup(markup);
        return photoMessage;
    }
    
    public static SendDocument createDocumentMessage(long chatId, InputFile inputFile, String caption, InlineKeyboardMarkup markup) {
        SendDocument documentMessage = new SendDocument();
        documentMessage.setChatId(String.valueOf(chatId));
        documentMessage.setDocument(inputFile);
        documentMessage.setCaption(caption);
        documentMessage.setReplyMarkup(markup);
        return documentMessage;
    }
}
