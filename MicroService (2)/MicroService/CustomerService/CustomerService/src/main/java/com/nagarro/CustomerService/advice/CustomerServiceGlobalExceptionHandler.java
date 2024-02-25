package com.nagarro.CustomerService.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nagarro.CustomerService.dto.CustomErrorResponseDto;
import com.nagarro.CustomerService.exception.CustomException;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jdk.internal.org.jline.utils.Log;

@RestControllerAdvice
public class CustomerServiceGlobalExceptionHandler {

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<?>handleException(CustomException ex) {

		  return ResponseEntity.internalServerError().body(ex.getMessage());
	}

}

