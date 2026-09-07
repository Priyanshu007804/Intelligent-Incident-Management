package com.priyanshu.iims.service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.priyanshu.iims.model.User;
import com.priyanshu.iims.model.enums.Role;
import com.priyanshu.iims.repository.UserRepository;
import com.priyanshu.iims.security.JwtService;
import com.priyanshu.iims.exception.InvalidCredentialsException;

@Service
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	
	public UserService( UserRepository userRepository,PasswordEncoder passwordEncoder, JwtService jwtService) {
		this.passwordEncoder=passwordEncoder;
		this.userRepository=userRepository;
		this.jwtService=jwtService;
	}
	
	public User registerUser(String email, String password) {
		String hashedPassword=passwordEncoder.encode(password);
		User user= new User(email, hashedPassword, Role.USER);
		
		return userRepository.save(user);
	}
	public String loginUser(String email, String password) {

	    User user = userRepository.findByEmail(email);

	    if (user == null ||
	            !passwordEncoder.matches(password, user.getPassword())) {
	        throw new InvalidCredentialsException("Invalid email or password");
	    }

	    return jwtService.generateToken(user.getEmail());
	}
}
