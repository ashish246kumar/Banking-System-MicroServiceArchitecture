package com.nagarro.CustomerService.service;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.nagarro.CustomerService.dto.CustomerDto;
import com.nagarro.CustomerService.dto.CustomerDtoResponse;
import com.nagarro.CustomerService.dto.CustomerUpdateDto;
import com.nagarro.CustomerService.exception.CustomException;

public interface CustomerServiceInterface {

	public void addCustomer(CustomerDto customerDto) throws CustomException;
	
	public boolean checkCustomerExist(int customerId);
	public List<CustomerDtoResponse> getAllCustomer() throws CustomException;
	
	public CustomerDtoResponse getSingleCustomer(int customerId) throws CustomException;
	
	public void updateCustomer(CustomerUpdateDto customerupdateDto) throws CustomException;
	
	public void deleteCustomer(int customerId) throws CustomException, JsonMappingException, JsonProcessingException;
}
