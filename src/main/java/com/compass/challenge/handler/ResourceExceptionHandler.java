package com.compass.challenge.handler;

import javax.servlet.http.HttpServletRequest;

import com.compass.challenge.exceptions.ChargeNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

import com.compass.challenge.domain.ErrorDetails;
import com.compass.challenge.domain.ErrorDetails.ErrorDetailsBuilder;
import com.compass.challenge.exceptions.SellerNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 
 * This class has methods to specifically handle exceptions thrown by request handling (@RequestMapping)
 *
 */
@ControllerAdvice
public class ResourceExceptionHandler {
	
	/**
	 * Convert a SellerNotFoundException to a ErrorDetails object
	 * 
	 * @param e - The SellerNotFoundException
	 * @param request - The HttpServletRequest
	 * @return - HttpStatus.NOT_FOUND with a ErrorDetails
	 */
	@ExceptionHandler(SellerNotFoundException.class)
	public ResponseEntity<ErrorDetails> handleSellerNotFoundException
							(SellerNotFoundException e, HttpServletRequest request) {
		
		ErrorDetailsBuilder builder = new ErrorDetailsBuilder();
		ErrorDetails error = builder.status(404l)
				.message("The seller was not found.")
				.timestamp(System.currentTimeMillis())
				.build();

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}

	/**
	 * Convert a ChargeNotFoundException to a ErrorDetails object
	 *
	 * @param e - The ChargeNotFoundException
	 * @param request - The HttpServletRequest
	 * @return - HttpStatus.NOT_FOUND with a ErrorDetails
	 */
	@ExceptionHandler(ChargeNotFoundException.class)
	public ResponseEntity<ErrorDetails> handleException(ChargeNotFoundException e, HttpServletRequest request) {

		ErrorDetailsBuilder builder = new ErrorDetailsBuilder();
		ErrorDetails error = builder.status(404l)
				.message("The charge was not found.")
				.timestamp(System.currentTimeMillis())
				.build();

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}
}
