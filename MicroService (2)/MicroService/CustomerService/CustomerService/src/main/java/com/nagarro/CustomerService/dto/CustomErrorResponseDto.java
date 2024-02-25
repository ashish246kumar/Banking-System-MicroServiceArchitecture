package com.nagarro.CustomerService.dto;

import org.springframework.http.HttpStatus;

public class CustomErrorResponseDto {

	@Override
	public String toString() {
		  return errorMessage;
	}
	private HttpStatus status;
    private String errorMessage;
	public HttpStatus getStatus() {
		return status;
	}
	public void setStatus(HttpStatus status) {
		this.status = status;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
    
}
