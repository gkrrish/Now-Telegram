package com.now.tele.external;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
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
public class InitalRequestService {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private MyTelegramBot telegramBot;

	private static final String BASE_URL = "http://localhost:9009/welcome";

	public void handleInitialRequest(String messageText, long chatId) throws IOException, TelegramApiException {
		WelcomeRequest welcomeRequest = new WelcomeRequest();
		welcomeRequest.setMobileNumber(Long.toString(chatId));
		welcomeRequest.setWelcomeAnyMessage(messageText);

		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", "application/json, application/pdf, image/png");

		HttpEntity<WelcomeRequest> httpEntity = new HttpEntity<>(welcomeRequest, headers);

		ResponseEntity<Resource> response = restTemplate.postForEntity(BASE_URL, httpEntity, Resource.class);
		String contentType = response.getHeaders().getContentType().toString();

		List<String> languages = response.getHeaders().getOrDefault("Languages", List.of("English")).stream()
									.flatMap(lang -> Arrays.stream(lang.replaceAll("[\\[\\]]", "").split(", ")))
									.collect(Collectors.toList());
		
		List<String> messages = response.getHeaders().getOrDefault("message", List.of("Welcome to NOW "));
		List<String> delta = response.getHeaders().getOrDefault("Delta", List.of("Welcome Back to NOW "));
		String message = String.join(" ", messages);
		String deltaMessage = String.join(" ", delta);

		
		if (contentType.equals("application/pdf")) {
			byte[] pdfData = response.getBody().getInputStream().readAllBytes();
			
			sendPdfResponse(chatId, pdfData, message, deltaMessage);
			
		} else if (contentType.equals("image/png")) {
			byte[] imageData = response.getBody().getInputStream().readAllBytes();
			
			sendImageResponse(chatId, imageData, message, languages);
			
		} else {
			sendTextResponse(chatId, "Unsupported response type");
		}
	}

	private void sendPdfResponse(long chatId, byte[] pdfData, String message, String delta) throws TelegramApiException {
		String todayDate = LocalDate.now().format(DateTimeFormatter.ofPattern("MMMYYYY"));
		InlineKeyboardMarkup markup = ButtonUtility.createInlineKeyboard(List.of(delta));

		InputFile inputFile = new InputFile(new ByteArrayInputStream(pdfData), todayDate + "_invoice.pdf");
		SendDocument documentMessage = TelegramMessageUtility.createDocumentMessage(chatId, inputFile, message, markup);
		telegramBot.execute(documentMessage);
	}

	private void sendImageResponse(long chatId, byte[] imageData, String message, List<String> languages) throws TelegramApiException {
		InlineKeyboardMarkup markup = ButtonUtility.createInlineKeyboard(languages, 1, 2);
		
		InputFile inputFile = new InputFile(new ByteArrayInputStream(imageData), "welcome-banner.png");
		SendPhoto photoMessage = TelegramMessageUtility.createPhotoMessage(chatId, inputFile, message, markup);
		telegramBot.execute(photoMessage);
	}

	private void sendTextResponse(long chatId, String messageText) throws TelegramApiException {
		InlineKeyboardMarkup markup = ButtonUtility.createInlineKeyboard(List.of("Option 1", "Option 2")); 
		
		SendMessage textMessage = TelegramMessageUtility.createTextMessage(chatId, messageText, markup);
		telegramBot.execute(textMessage);
	}
}
