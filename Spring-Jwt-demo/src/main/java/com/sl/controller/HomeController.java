package com.sl.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/home")
public class HomeController {
	
	@GetMapping("/demo")
	@PreAuthorize("hasAuthority('USER')")
	public String sayHello() {
		return "Hello This is a secured endpoint";
	}

}
