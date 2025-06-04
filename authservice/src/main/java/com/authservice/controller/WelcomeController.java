package com.authservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/welcome")
public class WelcomeController {
	
	//http://localhost:8080/swagger-ui/index.html
	
	@GetMapping("/message")
	public String welcome() {
		return "welcome";
	}
}
