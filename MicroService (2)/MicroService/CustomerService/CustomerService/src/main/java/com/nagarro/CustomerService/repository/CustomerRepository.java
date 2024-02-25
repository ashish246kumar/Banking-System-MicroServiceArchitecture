package com.nagarro.CustomerService.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nagarro.CustomerService.dto.CustomerDtoResponse;
import com.nagarro.CustomerService.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer,Integer>{


	

}
