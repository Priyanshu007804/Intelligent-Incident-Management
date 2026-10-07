package com.priyanshu.iims.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.priyanshu.iims.exception.InvalidCredentialsException;
import com.priyanshu.iims.model.User;
import com.priyanshu.iims.model.enums.Role;
import com.priyanshu.iims.repository.UserRepository;
import com.priyanshu.iims.security.JwtService;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private UserService userService;

    @Test
    void registerUserEncodesPasswordAndSavesUserWithUserRole() {
        User savedUser = new User("person@example.com", "encoded-password", Role.USER);
        when(passwordEncoder.encode("plain-password")).thenReturn("encoded-password");
        when(userRepository.save(org.mockito.ArgumentMatchers.any(User.class))).thenReturn(savedUser);

        User result = userService.registerUser("person@example.com", "plain-password");

        assertEquals("person@example.com", result.getEmail());
        assertEquals("encoded-password", result.getPassword());
        assertEquals(Role.USER, result.getRole());
        verify(userRepository).save(org.mockito.ArgumentMatchers.argThat(user ->
                user.getEmail().equals("person@example.com")
                        && user.getPassword().equals("encoded-password")
                        && user.getRole() == Role.USER));
    }

    @Test
    void loginReturnsTokenWhenCredentialsAreValid() {
        User user = new User("person@example.com", "encoded-password", Role.USER);
        when(userRepository.findByEmail("person@example.com")).thenReturn(user);
        when(passwordEncoder.matches("plain-password", "encoded-password")).thenReturn(true);
        when(jwtService.generateToken("person@example.com")).thenReturn("signed-token");

        String token = userService.loginUser("person@example.com", "plain-password");

        assertEquals("signed-token", token);
        verify(jwtService).generateToken("person@example.com");
    }

    @Test
    void loginRejectsUnknownUser() {
        when(userRepository.findByEmail("missing@example.com")).thenReturn(null);

        InvalidCredentialsException exception = assertThrows(InvalidCredentialsException.class,
                () -> userService.loginUser("missing@example.com", "any-password"));
        assertEquals("Invalid email or password", exception.getMessage());
    }

    @Test
    void loginRejectsIncorrectPassword() {
        User user = new User("person@example.com", "encoded-password", Role.USER);
        when(userRepository.findByEmail("person@example.com")).thenReturn(user);
        when(passwordEncoder.matches("wrong-password", "encoded-password")).thenReturn(false);

        InvalidCredentialsException exception = assertThrows(InvalidCredentialsException.class,
                () -> userService.loginUser("person@example.com", "wrong-password"));
        assertEquals("Invalid email or password", exception.getMessage());
    }
}