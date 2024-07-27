package com.now.tele.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.GetMe;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.now.tele.configurations.MyTelegramBot;

@RestController
public class BotCredentialsController {

	private final MyTelegramBot telegramBot;

	public BotCredentialsController(MyTelegramBot telegramBot) {
		this.telegramBot = telegramBot;
	}

	@GetMapping("/test-credentials")
	public String testCredentials() {
		try {
			GetMe getMe = new GetMe();
			User botUser = telegramBot.execute(getMe);
			return "Bot is working: userName :" + botUser.getUserName() + " FirstName :" + botUser.getFirstName() + " LastName :"
					+ botUser.getLastName() + " isBot :" + botUser.getIsBot() + " LanguageCode :" + botUser.getLanguageCode();
		} catch (TelegramApiException e) {
			e.printStackTrace();
			return "Failed to verify bot credentials: " + e.getMessage();
		}
	}
}
