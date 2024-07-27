package com.now.tele.external;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ExternalService {

	@Autowired
	private RestTemplate restTemplate;

	public void handleInitialRequest(String messageText, long chatId) {
		
		String url = "http://localhost:9009/welcome";
         restTemplate.getForObject(url, String.class);

	}

}
