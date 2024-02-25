package com.nagarro.accountService.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClientRequestException;

import com.nagarro.accountService.dto.AccountDtoResponse;
import com.nagarro.accountService.dto.AcountDtoRequest;
import com.nagarro.accountService.exception.CustomException;
import com.nagarro.accountService.service.AccountService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.net.ConnectException;

@RestController
@RequestMapping("/account")
@Slf4j
public class AccountController {

	@Autowired
	private AccountService accountService;
	

//	add Money
	@PostMapping("/addMoney")
	@CircuitBreaker(name = "account", fallbackMethod = "addMoneyfallbackMethod")
	@RateLimiter(name = "account", fallbackMethod = "getMessageFallBack")
	public ResponseEntity<String>addMoney(@RequestBody AcountDtoRequest acountDtoRequest){
		try {
		String message=accountService.addMoney(acountDtoRequest);
		return ResponseEntity.status(HttpStatus.OK).body(message);
		}
		catch (CustomException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}
	
//	fallback method
	
	public ResponseEntity<String>addMoneyfallbackMethod(AcountDtoRequest acountDtoRequest,WebClientRequestException e ){
		
		log.error("Customer Service is down");
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Some thing went wrong!! please try after some time");
	}
	
//	withdraw money
	
	@GetMapping("/getMoney/{actId}")
	public ResponseEntity<String>withDrawMoney(@PathVariable int actId,@RequestParam double balance) throws CustomException{
	 accountService.withDrawMoney(actId,balance);
	 return ResponseEntity.status(HttpStatus.OK).body("Amount Withdrawn Sucessfully");
	}
	
//	Get Account information
	
	@GetMapping("/getAccountDetail/{actId}")
	@CircuitBreaker(name = "account", fallbackMethod = "accountDetailfallbackMethod")
	@RateLimiter(name = "account", fallbackMethod = "getMessageFallBack")
	public ResponseEntity<?>accountDetail(@PathVariable int actId){
		try {
			return ResponseEntity.status(HttpStatus.OK).body(accountService.accountInfo(actId));
		} catch (CustomException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}
	
//	fallback method
      public ResponseEntity<AccountDtoResponse>accountDetailfallbackMethod(int actId,WebClientRequestException e){
    	  
    	  return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
    	            .body(new AccountDtoResponse());
	}
      
      public ResponseEntity<?> getMessageFallBack(RequestNotPermitted exception) {

          log.info("Rate limit has applied, So no further calls are getting accepted");

          return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
          .body("Too many requests : No further request will be accepted. Please try after sometime");
      }
      
//	delete account by customer id
	@DeleteMapping("/deleteAccount")
	public ResponseEntity<String>deleteAccountByCustomerId(@RequestParam("customerId") int customerId) throws CustomException{
	
		accountService.deleteAccountByCustomerId(customerId);
		return ResponseEntity.status(HttpStatus.OK).body("Account Deleted Sucessfully");
		
	}
	
//	delete account by account id
	
	@DeleteMapping("/delete/{actId}")
	public ResponseEntity<String> deleteAccountByActId(@PathVariable int actId) throws CustomException{
		
		accountService.deleteAccountByActId(actId);
		return ResponseEntity.status(HttpStatus.OK).body("Account Deleted Sucessfully");
	}
	
}
