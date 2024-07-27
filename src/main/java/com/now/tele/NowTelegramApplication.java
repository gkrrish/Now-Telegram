package com.now.tele;

import org.apache.catalina.valves.AccessLogValve;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class NowTelegramApplication {

	public static void main(String[] args) {
		SpringApplication.run(NowTelegramApplication.class, args);
	}
	@Bean
	public TomcatServletWebServerFactory tomcatFactory() {
	    TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
	    // Disable APR
	    factory.addContextCustomizers(context -> {
	        context.getPipeline().addValve(new AccessLogValve());
	    });
	    return factory;
	}


}
