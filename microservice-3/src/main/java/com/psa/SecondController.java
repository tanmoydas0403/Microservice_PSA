package com.psa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecondController {
	
	@Autowired
	private Client client;
	
	@GetMapping("/fromsecondcontroller")
	public String getMessageFromMicroservices1() {
		return client.getData();
	}

}