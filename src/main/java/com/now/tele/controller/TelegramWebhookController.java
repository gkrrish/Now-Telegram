package com.now.tele.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
public class TelegramWebhookController {

	@Autowired
	private ProxyService proxyService;

	@PostMapping("/webhook")
	public void onUpdateReceived(@RequestBody Update update) {

		try {
			 proxyService.telegramMessages(update);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
