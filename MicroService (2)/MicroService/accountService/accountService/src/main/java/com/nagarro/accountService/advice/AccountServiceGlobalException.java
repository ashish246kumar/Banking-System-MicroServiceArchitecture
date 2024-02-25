package com.nagarro.accountService.advice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClient;


import com.nagarro.accountService.dto.CustomErrorResponseDto;
import com.nagarro.accountService.exception.CustomException;
import com.nagarro.accountService.repository.AccountRepositoy;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;


@RestControllerAdvice
public class AccountServiceGlobalException {

	
	@ExceptionHandler(CustomException.class)
	public ResponseEntity<?>handleException(CustomException ex){

		  
		  return ResponseEntity.internalServerError().body(ex.getMessage());
	}
	

}
