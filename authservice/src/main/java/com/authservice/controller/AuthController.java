package com.authservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.authservice.dto.APIResponse;
import com.authservice.dto.LoginDto;
import com.authservice.dto.UserDto;
import com.authservice.repository.UserRepository;
import com.authservice.service.AuthService;
import com.authservice.service.JwtService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
	
	@Autowired
	private AuthService authService;
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserRepository repository;

	@PostMapping("/register")
	public ResponseEntity<APIResponse<String>> register(@RequestBody UserDto dto){
		APIResponse<String> response = authService.register(dto);
		return new ResponseEntity<>(response, HttpStatusCode.valueOf(response.getStatus()));
	}
	
	@PostMapping("/login")
	public ResponseEntity<APIResponse<String>> login(@RequestBody LoginDto dto){
		
		APIResponse<String> response=new APIResponse<>();
		
		UsernamePasswordAuthenticationToken token=
				new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword());
		
		try {
			
			Authentication authenticate = authenticationManager.authenticate(token);
			
			if(authenticate.isAuthenticated()) {
				String jwtToken = jwtService.generateToken(dto.getUsername(), 
						authenticate.getAuthorities().iterator().next().getAuthority());
				
				response.setMessage("Login Sucessful");
				response.setStatus(200);
				response.setData(jwtToken);
				return new ResponseEntity<>(response, HttpStatusCode.valueOf(response.getStatus()));
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		response.setMessage("Failed");
		response.setStatus(401);
		response.setData("Un-Authorized Access");
		return new ResponseEntity<APIResponse<String>>(response,HttpStatusCode.valueOf(response.getStatus()));
	}
	
	@GetMapping("/get-user")
	public com.authservice.entity.User getUserByUserName(@RequestParam String username) {
		com.authservice.entity.User user = repository.findByUsername(username);
		return user;
	}
	
}