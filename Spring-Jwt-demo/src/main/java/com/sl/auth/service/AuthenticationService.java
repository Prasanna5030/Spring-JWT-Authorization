package com.sl.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sl.auth.AuthenticationRequest;
import com.sl.auth.AuthentionResponse;
import com.sl.auth.RegisterRequest;
import com.sl.config.JwtService;
import com.sl.entity.User;
import com.sl.enums.Role;
import com.sl.repository.UserRepository;

@Service
public class AuthenticationService {
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	public AuthentionResponse register(RegisterRequest registerRequest) {
		var user= User.builder()
				.name(registerRequest.getUsername())
				.password(passwordEncoder.encode(registerRequest.getPassword()))
				.email(registerRequest.getEmail())
				.role(Role.USER)
				.build();
		
		userRepository.save(user);
		var JwtToken= jwtService.generateToken(user);
		
		return AuthentionResponse.builder()
				.jwtToken(JwtToken)
				.build();
	}

	public AuthentionResponse authenticate(AuthenticationRequest authenticationRequest) {
		
	authenticationManager.authenticate(
		new UsernamePasswordAuthenticationToken(
				authenticationRequest.getUsername(), 
				authenticationRequest.getPassword())	
			);
	
	var user = userRepository.findByName(authenticationRequest.getUsername()).orElseThrow();
	
	var JwtToken= jwtService.generateToken(user);
	
	return AuthentionResponse.builder()
			.jwtToken(JwtToken)
			.build();
	
	}
	

	
	

}
