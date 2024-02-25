package com.nagarro.CustomerService.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.nagarro.CustomerService.dto.CustomerDto;
import com.nagarro.CustomerService.exception.CustomException;
import com.nagarro.CustomerService.service.CustomerService;
import com.nagarro.CustomerService.service.CustomerServiceInterface;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;

import com.nagarro.CustomerService.dto.CustomerDtoResponse;
import com.nagarro.CustomerService.dto.CustomerUpdateDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/customer")
public class CustomerController {


	@Autowired
	private CustomerServiceInterface customerService;
	
//	add customer
	
	@PostMapping("/addCustomer")
	private ResponseEntity<String> addCustomer(@RequestBody CustomerDto customerDto) throws CustomException {

		customerService.addCustomer(customerDto);

		return ResponseEntity.status(HttpStatus.OK).body("Customer Added SucessFully");
	}
//	#getCustomer
	
	@GetMapping("/getAllCustomer")
	private ResponseEntity<List<CustomerDtoResponse>> getAllCustomer() throws CustomException{
	
		List<CustomerDtoResponse> customers=customerService.getAllCustomer();
		
		return ResponseEntity.status(HttpStatus.OK).body(customers);
	}
//	get customerbyid
	
	@GetMapping("/singleCustomer")
	private ResponseEntity<CustomerDtoResponse> getSingleCustomer(@RequestParam int customerId) throws CustomException{
		
		
		return ResponseEntity.status(HttpStatus.OK).body(customerService.getSingleCustomer(customerId));
	}
	
//update customer
	
	@PutMapping("/updateCustomer")
	private ResponseEntity<String>updateCustomer(@RequestBody CustomerUpdateDto customerUpdateDto) throws CustomException{
		
		customerService.updateCustomer(customerUpdateDto);
		return ResponseEntity.status(HttpStatus.OK).body("customer updated sucessfully");
		
	}
//	checkCustomerExist	
	@GetMapping("/customerExist")
	public boolean checkCustomerExist(@RequestParam int customerId) {
		return customerService.checkCustomerExist(customerId);
	}
	
//	delete customer
	
	@DeleteMapping("/deleteCustomer/{customerId}")
//	@CircuitBreaker(name = "account", fallbackMethod = "fallbackMethod")
	public ResponseEntity<String> deleteCustomer(@PathVariable("customerId") int customerId) throws CustomException, JsonMappingException, JsonProcessingException{
		
		customerService.deleteCustomer(customerId);
		return ResponseEntity.status(HttpStatus.OK).body("customer deleted sucessfully");
	}
	
//	fallback method
	public ResponseEntity<String>fallbackMethod(int customerId,Exception runtimeException){
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Some thing went wrong!! please try after some time");
	}
	
}
