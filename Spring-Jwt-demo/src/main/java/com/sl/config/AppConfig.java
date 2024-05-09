package com.sl.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.sl.repository.UserRepository;



@EnableWebSecurity
@Configuration
@EnableMethodSecurity
public class AppConfig {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	@Lazy
	private JwtAuthenticationFilter  jwtAuthenticationFilter;
	@Bean
	public UserDetailsService userDetailsService() {
		UserDetailsService userDetailsService=  username ->userRepository.findByName(username)
																.orElseThrow(()->new UsernameNotFoundException("user with "+username+"not found"));
		
		return userDetailsService;
		}	
	@Bean

	    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	        http
	               .csrf(AbstractHttpConfigurer::disable)
	                .authorizeHttpRequests(req ->
	                        req.requestMatchers("/api/auth/**")
	                                .permitAll()
	                                .anyRequest()
	                     
	                                .authenticated()
	                )
	                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	                .authenticationProvider(authenticationProvider())
	                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
	        ;

	        return http.build();
	    }
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider= new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService());
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		return authenticationProvider;
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
	return	config.getAuthenticationManager();
		
	}
}
