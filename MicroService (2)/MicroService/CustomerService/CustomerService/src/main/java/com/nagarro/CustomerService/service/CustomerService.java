package com.nagarro.CustomerService.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nagarro.CustomerService.dto.AccountDto;
import com.nagarro.CustomerService.dto.Constant;
import com.nagarro.CustomerService.dto.CustomErrorResponseDto;
import com.nagarro.CustomerService.dto.CustomerDto;
import com.nagarro.CustomerService.dto.CustomerDtoResponse;
import com.nagarro.CustomerService.dto.CustomerUpdateDto;
import com.nagarro.CustomerService.entity.Customer;
import com.nagarro.CustomerService.exception.CustomException;
import com.nagarro.CustomerService.repository.CustomerRepository;

import jdk.internal.org.jline.utils.Log;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;

@Service
public class CustomerService implements CustomerServiceInterface{

	
	private final WebClient.Builder webClientBuilder;
	 private static  Logger log = LoggerFactory.getLogger(CustomerService.class);
	private CustomerRepository customerRepository;
	
	
	
	@Autowired
	public CustomerService(WebClient.Builder webClientBuilder,CustomerRepository customerRepository) {
		this.webClientBuilder=webClientBuilder;
		this.customerRepository=customerRepository;
	}
	
	@Override
	public void addCustomer(CustomerDto customerDto) throws CustomException {
		
		
		  try {
			Customer customer = new Customer();

			
			customer.setCustomerName(customerDto.getCustomerName());
			customer.setPhone(customerDto.getPhone());
			customer.setAddress(customerDto.getAddress());
			customerRepository.save(customer);
			log.info("Customer added successfully");
		  }
		  catch (Exception e) {
		        log.error("Error adding customer", e);
		        throw new CustomException("Error in adding customer");
		  }

	}
	@Override
	public List<CustomerDtoResponse> getAllCustomer() throws CustomException {
		try {
		List<CustomerDtoResponse>customers=customerRepository.findAll()
				.stream()
				.map(this::mapToDto).toList();
		if(customers.isEmpty()) {
			 log.error("No customers found in the database.");
			throw new CustomException("No Any Customer");
			
		}
		return customers;
		
		}
		catch(Exception e) {
			log.error("Error getting all customers", e);
	        throw new CustomException("Error getting all customers");
		}
		
		
	}
	private CustomerDtoResponse mapToDto(Customer customer) {
		 
		CustomerDtoResponse customerDtoResponse=new CustomerDtoResponse();
		customerDtoResponse.setCustomerId(customer.getCustomer_id());

		customerDtoResponse.setCustomerName(customer.getCustomerName());
		customerDtoResponse.setPhone(customer.getPhone());
		customerDtoResponse.setAddress(customer.getAddress());
		return customerDtoResponse;
	}
	
	@Override
	public CustomerDtoResponse getSingleCustomer(int customerId) throws CustomException  {
		try {
		Customer customer=customerRepository.findById(customerId).get();

        return mapToDto(customer);
		} catch (Exception e) {
		     log.error("Error getting customer with : {}", customerId);
		     throw new CustomException("Error getting customer");
		}
	}
	@Override
	public void updateCustomer(CustomerUpdateDto customerupdateDto) throws CustomException {
		try {
		Customer customer=customerRepository.findById(customerupdateDto.getCustomerId()).get();
		if(customer==null) {
	
			throw new CustomException("Customer not Exist So not able to update");
			
		}
		customer.setCustomerName(customerupdateDto.getCustomerName());
		customer.setAddress(customerupdateDto.getAddress());
		customer.setPhone(customerupdateDto.getPhone());
		customerRepository.save(customer);
         log.info("Customer updated successfully: {}", customerupdateDto.getCustomerId());
        } catch (Exception e) {
            log.error("Error updating customer with Id: {}", customerupdateDto.getCustomerId());
            throw e;
        }
		
		
		
	}

	@Override
	public boolean checkCustomerExist(int customerId) {
		
		Optional<Customer> customer=customerRepository.findById(customerId);
		if(customer.isPresent()) {
			return true;
		}
		return false;
		
	}
	
	@Override
	public void deleteCustomer(int customerId) throws CustomException, JsonMappingException, JsonProcessingException  {
		try {
		Optional<Customer> customer=customerRepository.findById(customerId);
		if(!customer.isPresent()) {
			log.error("error in deleting customer");
			throw new CustomException("Customer not Exist So not able to delete");
		}
		
		String message=webClientBuilder.build().delete().uri(Constant.ACCOUNT_SERVICE_URL+"/deleteAccount",
       		 uriBuilder -> uriBuilder.queryParam("customerId",customerId).build())
        .retrieve()
        .bodyToMono(String.class)
        .onErrorResume(error -> Mono.empty()) 
        .block();
		Customer customer1 = customer.get();
		customerRepository.delete(customer1);
		
		}
		
		catch (WebClientResponseException ex) {
	

            log.error("WebClientResponseException in deletingCustomer{}",ex.getResponseBodyAsString());
           throw new CustomException(ex.getResponseBodyAsString());
        } 
		catch(Exception e) {
			  log.error("Error in deleting customer with CustomerId{}",customerId);
			  throw e;

		}

		
	}
	
}
