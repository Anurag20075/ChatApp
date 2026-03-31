package com.chat_app.Controller;

// package com.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

	@GetMapping("/")
	public ResponseEntity<String> homeController() {
		return ResponseEntity.ok("Welcome to the Chat App API!");
	}
}

