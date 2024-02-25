package com.nagarro.accountService.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.context.annotation.Lazy;

import com.nagarro.accountService.dto.AccountDtoResponse;
import com.nagarro.accountService.dto.AcountDtoRequest;
import com.nagarro.accountService.dto.Constant;
import com.nagarro.accountService.dto.CustomerDtoResponse;
import com.nagarro.accountService.entity.Account;
import com.nagarro.accountService.exception.CustomException;
import com.nagarro.accountService.repository.AccountRepositoy;

import lombok.RequiredArgsConstructor;

@Service
//@RefreshScope
public class AccountServiceImpl implements AccountService{
	 private static  Logger log = LoggerFactory.getLogger(AccountServiceImpl.class);
	 
//	 @Lazy
	
	private final WebClient.Builder webClientBuilder;
	private AccountRepositoy accountRepository;
	
	@Autowired
	public AccountServiceImpl (WebClient.Builder webClientBuilder,AccountRepositoy accountRepository) {
		this.webClientBuilder=webClientBuilder;
		this.accountRepository=accountRepository;
	}
	
	public boolean checkCustomerExcist(int customerId) throws CustomException {
			try {
			return webClientBuilder.build().get()
            .uri(Constant.CUSTOMER_SERVICE_URL+"/customerExist",
           		 uriBuilder -> uriBuilder.queryParam("customerId",customerId).build())
            .retrieve()
            .bodyToMono(Boolean.class)
            .block(); 
			}
			catch (WebClientResponseException e) {
			       
		        log.error("Error occurred in customer service");
		        throw new CustomException(e.getResponseBodyAsString());
		    }
		}

	
	
	@Override
	public String addMoney(AcountDtoRequest acountDtoRequest) throws CustomException {
		
		Account accountExist=accountRepository.findByCustomerId(acountDtoRequest.getCustomerId());

		if (accountExist == null) {
		  
		        if (checkCustomerExcist(acountDtoRequest.getCustomerId())) {
		            Account account = new Account();
		            String accountNo = UUID.randomUUID().toString();
		            account.setCustomerId(acountDtoRequest.getCustomerId());
		            account.setAccountNo(accountNo);
		            account.setBalance(acountDtoRequest.getBalance());
		            account.setCreatedAt(LocalDate.now());
		            account.setUpdatedAt(LocalDate.now());
		            accountRepository.save(account);
		        } else {
		            log.error("Customer with this Account does not exist");
		            throw new CustomException("Customer with this Account does not exist");
		        }
		    
		} else {
		    double currentBalance = accountExist.getBalance();
		    double newBalance = currentBalance + acountDtoRequest.getBalance();
		    accountExist.setBalance(newBalance);
		    accountExist.setUpdatedAt(LocalDate.now());
		    accountRepository.save(accountExist);
		}
		
			return "Money added sucessfully";	
		
		
	}

	@Override
	public void withDrawMoney(int actId,double balance) throws CustomException {
		
		 Optional<Account> optionalAccount=getAccountById(actId);
		if(!optionalAccount.isPresent()) {
			log.error("Account not exist");
	        throw new CustomException("Customer with this account  does not exist");  
		}
		else {
			 Account accountExist = optionalAccount.get();
			double currentBalance = accountExist.getBalance();
			if(currentBalance<balance) {
				log.error("your blance is low");
		        throw new CustomException("your balance is low"); 
			}
			else {
				double newBalance = currentBalance-balance;
				System.out.print("new Balance"+newBalance);
				accountExist.setBalance(newBalance);
				accountExist.setUpdatedAt(LocalDate.now());
				accountRepository.save(accountExist);

			}
            				
		}

	}

	@Override
	public AccountDtoResponse accountInfo(int actId) throws CustomException {
		 Optional<Account> optionalAccount=getAccountById(actId);
		if(!optionalAccount.isPresent()) {
			log.error("Account not exist ");
	        throw new CustomException(" Account  does not exist");
		}
		 Account accountExist = optionalAccount.get();
		CustomerDtoResponse customer=findCustomer(accountExist.getCustomerId());
		AccountDtoResponse accountDtoResponse=new  AccountDtoResponse();
		accountDtoResponse.setCustomerName(customer.getCustomerName());
		accountDtoResponse.setPhone(customer.getPhone());
		accountDtoResponse.setAddress(customer.getAddress());
		accountDtoResponse.setAccountNo(accountExist.getAccountNo());
		accountDtoResponse.setBalance(accountExist.getBalance());
		accountDtoResponse.setUpdatedAt(accountExist.getUpdatedAt());
		return accountDtoResponse;
	}
	@Override
	public void deleteAccountByActId(int actId) throws CustomException {
		try {
			 Optional<Account> optionalAccount=getAccountById(actId);
		
		if(optionalAccount.isPresent()) {
			 Account accountExist = optionalAccount.get();
			accountRepository.delete(accountExist);
		}
		else {
			log.error("Account Not Exist");
			throw new CustomException("Account not Exist");
		}
		 
		}
		catch(Exception e) {

			throw e;
		}
	}
	@Override
	public void deleteAccountByCustomerId(int customerId) throws CustomException {
		try {
		Account accountExist=accountRepository.findByCustomerId(customerId);
		if(accountExist!=null) {
			accountRepository.delete(accountExist);
		}
		else {

			throw new CustomException("Account not Exist");
		}
		 
		}
		catch(Exception e) {


			throw e;
		}
	}
	
	private Optional<Account> getAccountById(int actId) {
	    return accountRepository.findById(actId);
	}
	
	public CustomerDtoResponse findCustomer(int customerid) throws CustomException {
		try {
		return webClientBuilder.build().get()
	             .uri(Constant.CUSTOMER_SERVICE_URL+"/singleCustomer",
	            		 uriBuilder -> uriBuilder.queryParam("customerId",customerid).build())
	             .retrieve()
	             .bodyToMono(CustomerDtoResponse.class)
	             .block(); 
		}
		catch (WebClientResponseException e) {
		       
	        log.error("Error occurred in customer service");
	        throw new CustomException(e.getResponseBodyAsString());
	    }
	}
}
