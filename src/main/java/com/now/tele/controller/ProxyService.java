package com.now.tele.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.now.tele.external.InitalRequestService;

/**
 * This class is meanly meant for shadow of web-hook controller.
 */
@Service
public class ProxyService {
	
	@Autowired
	private InitalRequestService externalService;

	public void telegramMessages(Update update) throws Exception {
		if (update.hasMessage() && update.getMessage().hasText()) {
			String messageText = update.getMessage().getText();
			long chatId = update.getMessage().getChatId();
			
			anyMessageIntialRequest(messageText, chatId);
			
		} else if (update.hasCallbackQuery()) {
			String callbackData = update.getCallbackQuery().getData();
			long chatId = update.getCallbackQuery().getMessage().getChatId();
			// Handle callback query here
		}
	}
	
	public void anyMessageIntialRequest(String messageText, long chatId) throws Exception {
		externalService.handleInitialRequest(messageText, chatId);
	}

}
