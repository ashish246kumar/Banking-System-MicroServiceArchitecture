package com.nagarro.accountService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDtoResponse {

	private int customerId;
	private String customerName;
	private String phone;
	private String address;
	
}
