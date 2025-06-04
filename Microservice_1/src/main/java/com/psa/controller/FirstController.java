package com.psa.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FirstController {
	
	@GetMapping("/message")
	public String getMsg() {
		return "Welcome to First Microservice";
	}

}
