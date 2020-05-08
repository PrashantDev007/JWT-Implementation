package com.security.securityjwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.security.securityjwt.models.AuthenticationRequest;
import com.security.securityjwt.models.AuthenticationResponse;
import com.security.securityjwt.services.MyUserDetailsService;
import com.security.securityjwt.util.JwtUtil;



@RestController
public class HelloController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private MyUserDetailsService userDetailsService;
	
	@Autowired
	private JwtUtil jwtTokenUtil;
	
	@RequestMapping("/hello")
	public String hello()
	{
		System.out.println("-------");	
		return "Hello world";
	}
	
	@PostMapping("/authenticate")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception{
		
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
					);
		}
		catch (BadCredentialsException e) {
			throw new Exception("incorrect name or password " +e);
		}
		
		final UserDetails userDetails=userDetailsService
				.loadUserByUsername( authenticationRequest.getUsername());
		
		final String jwt=jwtTokenUtil.generateToken(userDetails);
	
		return ResponseEntity.ok(new AuthenticationResponse(jwt));
	}
	
	
	
	
	
	
	
	
}
