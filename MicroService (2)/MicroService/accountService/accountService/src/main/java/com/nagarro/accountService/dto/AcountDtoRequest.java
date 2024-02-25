package com.nagarro.accountService.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AcountDtoRequest {

	private int customerId ;
	private double balance;

	
	
}
