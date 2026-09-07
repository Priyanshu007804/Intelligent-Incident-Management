package com.priyanshu.iims.exception;
import java.time.LocalDateTime;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.converter.HttpMessageNotReadableException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.MethodArgumentNotValidException;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(InvalidAssignmentException.class)
	public ResponseEntity<ErrorResponse> handleInvalidAssignment(
	        InvalidAssignmentException exception,
	        HttpServletRequest request) {

	    ErrorResponse errorResponse = new ErrorResponse(
	            LocalDateTime.now(),
	            HttpStatus.BAD_REQUEST.value(),
	            "Bad Request",
	            exception.getMessage(),
	            request.getRequestURI()
	    );

	    return ResponseEntity
	            .status(HttpStatus.BAD_REQUEST)
	            .body(errorResponse);
	}
	
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ErrorResponse> handleAccessDenied(
	        AccessDeniedException exception,
	        HttpServletRequest request) {

	    ErrorResponse errorResponse = new ErrorResponse(
	            LocalDateTime.now(),
	            HttpStatus.FORBIDDEN.value(),
	            "Forbidden",
	            "Access denied",
	            request.getRequestURI()
	    );

	    return ResponseEntity
	            .status(HttpStatus.FORBIDDEN)
	            .body(errorResponse);
	}
	@ExceptionHandler(InvalidCredentialsException.class)
	public ResponseEntity<ErrorResponse> handleInvalidCredentials(
	        InvalidCredentialsException exception,
	        HttpServletRequest request) {

	    ErrorResponse errorResponse = new ErrorResponse(
	            LocalDateTime.now(),
	            HttpStatus.UNAUTHORIZED.value(),
	            "Unauthorized",
	            exception.getMessage(),
	            request.getRequestURI()
	    );

	    return ResponseEntity
	            .status(HttpStatus.UNAUTHORIZED)
	            .body(errorResponse);
	}
	@ExceptionHandler(InvalidStatusTransitionException.class)
	public ResponseEntity<ErrorResponse> handleInvalidStatusTransition(
	        InvalidStatusTransitionException exception,
	        HttpServletRequest request) {

	    ErrorResponse errorResponse = new ErrorResponse(
	            LocalDateTime.now(),
	            HttpStatus.CONFLICT.value(),
	            "Conflict",
	            exception.getMessage(),
	            request.getRequestURI()
	    );

	    return ResponseEntity
	            .status(HttpStatus.CONFLICT)
	            .body(errorResponse);
	}
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleResourceNotFound(
	        ResourceNotFoundException exception,
	        HttpServletRequest request) {

	    ErrorResponse errorResponse = new ErrorResponse(
	            LocalDateTime.now(),
	            HttpStatus.NOT_FOUND.value(),
	            "Not Found",
	            exception.getMessage(),
	            request.getRequestURI()
	    );

	    return ResponseEntity
	            .status(HttpStatus.NOT_FOUND)
	            .body(errorResponse);
	}
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidationException(
	        MethodArgumentNotValidException exception,
	        HttpServletRequest request) {

	    String message = exception.getBindingResult()
	            .getFieldErrors()
	            .stream()
	            .map(error -> error.getField() + ": " + error.getDefaultMessage())
	            .collect(Collectors.joining(", "));

	    ErrorResponse errorResponse = new ErrorResponse(
	            LocalDateTime.now(),
	            HttpStatus.BAD_REQUEST.value(),
	            "Validation Failed",
	            message,
	            request.getRequestURI()
	    );

	    return ResponseEntity
	            .status(HttpStatus.BAD_REQUEST)
	            .body(errorResponse);
	}
	
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(
	        HttpMessageNotReadableException exception,
	        HttpServletRequest request) {

	    ErrorResponse errorResponse = new ErrorResponse(
	            LocalDateTime.now(),
	            HttpStatus.BAD_REQUEST.value(),
	            "Bad Request",
	            "Invalid request data. Please check the provided values.",
	            request.getRequestURI()
	    );

	    return ResponseEntity
	            .status(HttpStatus.BAD_REQUEST)
	            .body(errorResponse);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGlobalException(Exception exception){
		ErrorResponse errorResponse= new ErrorResponse(LocalDateTime.now(),HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error", exception.getMessage(),null );
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
	}
	

}
