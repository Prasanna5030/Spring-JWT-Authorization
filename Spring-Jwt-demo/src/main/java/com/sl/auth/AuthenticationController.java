package com.sl.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sl.auth.service.AuthenticationService;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
	
	@Autowired
	private AuthenticationService authenticationService;
	
	@PostMapping("/register")
	public ResponseEntity<AuthentionResponse> register(@RequestBody RegisterRequest  registerRequest ){
		
		return new ResponseEntity<AuthentionResponse>(authenticationService.register(registerRequest), HttpStatus.ACCEPTED);
	}
	
	@PostMapping("/authenticate")
 public ResponseEntity<AuthentionResponse> register(@RequestBody AuthenticationRequest  authenticationRequest ){
	
	return new ResponseEntity<AuthentionResponse>(authenticationService.authenticate(authenticationRequest), HttpStatus.ACCEPTED);
		
		
	}
}
