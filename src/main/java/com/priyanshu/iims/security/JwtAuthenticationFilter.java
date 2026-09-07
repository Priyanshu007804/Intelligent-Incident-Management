package com.priyanshu.iims.security;
import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.priyanshu.iims.model.User;
import com.priyanshu.iims.repository.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public class JwtAuthenticationFilter extends OncePerRequestFilter{
	private final JwtService jwtService;
	private final UserRepository userRepository;
	public JwtAuthenticationFilter(JwtService jwtService, UserRepository userRepository) {
		this.jwtService=jwtService;
		this.userRepository=userRepository;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException{
		String authHeader= request.getHeader("Authorization");
		if(authHeader==null || !authHeader.startsWith("Bearer")) {
			filterChain.doFilter(request, response);
			return;
		}
		
		
		String token=authHeader.substring(7);
		if(jwtService.isTokenValid(token)) {
			String email=jwtService.extractUsername(token);
			User user=userRepository.findByEmail(email);
			
			if(user!=null) {
				SimpleGrantedAuthority authority=new SimpleGrantedAuthority("ROLE_"+user.getRole().name());
				
				UsernamePasswordAuthenticationToken authentication= new UsernamePasswordAuthenticationToken(user.getEmail(),null,java.util.List.of(authority));
				
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
					
		}
		
		
		filterChain.doFilter(request, response);
		
		
	}

}
