package com.sl.config;

import java.security.Key;
import java.util.Date;

import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;


@Service
public class JwtService {

	private static final String SECRET_KEY="190facf11cb915e5ce544723fe68c6cd1252ab8de42627e18e69c4031d7b869b";

	
	public String generateToken( Map<String, Object> extraClaims, UserDetails userDetails) {
		return Jwts
				.builder()
				.addClaims(extraClaims)
				.setSubject(userDetails.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis()+ 1000*60 *60 *24))
				.signWith(getSigninKey(), SignatureAlgorithm.HS256)
				.compact();

	}
	
	public String generateToken( UserDetails userDetails) {
		return  generateToken(new TreeMap<String, Object>() , userDetails);
	}
           
	public Boolean isTokenValid( String jwtToken, UserDetails userDetails) {
		final String username= extractUserName(jwtToken);
		return (username.equals(userDetails.getUsername()) && isTokenExpired(jwtToken));
		
	}
	
	private boolean isTokenExpired(String jwtToken) {
		return extractExpiration(jwtToken).before(new Date());
	}

	// extracts the expiration date from claims of jwt token
	
	private Date extractExpiration(String jwtToken) {
		return extractClaim(jwtToken, Claims::getExpiration);
	}

	// extracts the expiration username from claims of jwt token
	
	public String extractUserName(String jwtToken) {
		return extractClaim( jwtToken , Claims::getSubject );
	}

	
	
	public <T> T extractClaim(String jwtToken, Function<Claims, T > claimsResolver) {
		final Claims claims=extractAllClaims(jwtToken);
		return claimsResolver.apply(claims);
	}
	
	public Claims extractAllClaims(String jwtToken) {
		return Jwts
				.parserBuilder()
				.setSigningKey(getSigninKey())
				.build()
				.parseClaimsJws(jwtToken)
				.getBody();	
	}
	
	private Key getSigninKey() {
		byte[] keyBites =Decoders.BASE64.decode(SECRET_KEY);
		return Keys.hmacShaKeyFor(keyBites);
	}
}
