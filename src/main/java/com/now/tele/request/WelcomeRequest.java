package com.now.tele.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WelcomeRequest {

	private String welcomeAnyMessage;

	@NotBlank(message = "Mobile number required")
	@Pattern(regexp = "^\\+91[1-9]\\d{9}$", message = "Invalid Indian mobile number")
	private String mobileNumber;

}
