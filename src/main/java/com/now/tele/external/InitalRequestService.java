package com.now.tele.external;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
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

import reactor.core.publisher.Mono;

@Service
public class InitalRequestService {

	@Autowired
	private WebClient webClient;

	@Autowired
	private MyTelegramBot telegramBot;
	
	private static final String WELCOME_URL = "/welcome";

    public void handleInitialRequest(String messageText, long chatId) throws IOException, TelegramApiException, Exception {
        WelcomeRequest welcomeRequest = new WelcomeRequest();
        welcomeRequest.setMobileNumber(Long.toString(chatId));
        welcomeRequest.setWelcomeAnyMessage(messageText);

        webClient.post().uri(WELCOME_URL) .bodyValue(welcomeRequest).exchangeToFlux(response -> response.bodyToFlux(byte[].class)
                .map(responseBody -> { 
                	
                	String contentType = response.headers().contentType().map(MediaType::toString).orElse(null);
                	
                	try {
                		if (contentType != null && (contentType.equals("image/png") || contentType.equals("image/jpeg"))) {
                			List<String> languages = response.headers().header("Languages"); 
                			List<String> message = response.headers().header("message");
                            sendImageResponse(chatId, responseBody, message.get(0), languages);
                            
                        } else if (contentType != null && contentType.equals("application/pdf")) {
                        	List<String> message = response.headers().header("message");
                        	List<String> delta = response.headers().header("Delta");
                            sendPdfResponse(chatId, responseBody, message.get(0), delta.get(0));
                            
                        } else {
                            sendTextResponse(chatId, new String(responseBody));
                        }
                		
                	}catch (TelegramApiException e) {
                		e.printStackTrace();
					}

                    return Mono.empty();
                }))
            .blockFirst();
    }


	private void sendPdfResponse(long chatId, byte[] pdfData, String message, String delta) throws TelegramApiException {
		String todayDate = LocalDate.now().format(DateTimeFormatter.ofPattern("MMMYYYY"));
		InlineKeyboardMarkup markup = ButtonUtility.createInlineKeyboard(List.of(delta));

		InputFile inputFile = new InputFile(new ByteArrayInputStream(pdfData), todayDate + "_invoice.pdf");
		SendDocument documentMessage = TelegramMessageUtility.createDocumentMessage(chatId, inputFile, message, markup);
		telegramBot.execute(documentMessage);
	}

	private void sendImageResponse(long chatId, byte[] imageData, String message, List<String> languages) throws TelegramApiException {
		InlineKeyboardMarkup markup = ButtonUtility.createInlineKeyboard(languages);
		

	    // Logging image data
	    System.out.println("Image data length: " + imageData.length);
		
		InputFile inputFile = new InputFile(new ByteArrayInputStream(imageData), "welcome-banner.png");
		
		  
	    // Temporarily save image for inspection
	    try (FileOutputStream fos = new FileOutputStream("test_image.png")) {
	        fos.write(imageData);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
		
		SendPhoto photoMessage = TelegramMessageUtility.createPhotoMessage(chatId, inputFile, message, markup);
		telegramBot.execute(photoMessage);
	}

	private void sendTextResponse(long chatId, String messageText) throws TelegramApiException {
		InlineKeyboardMarkup markup = ButtonUtility.createInlineKeyboard(List.of("Option 1", "Option 2")); 
		
		SendMessage textMessage = TelegramMessageUtility.createTextMessage(chatId, messageText, markup);
		telegramBot.execute(textMessage);
	}
}
