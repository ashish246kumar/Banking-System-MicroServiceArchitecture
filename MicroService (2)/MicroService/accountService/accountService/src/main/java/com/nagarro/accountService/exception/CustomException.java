package com.nagarro.accountService.exception;

public class CustomException extends Exception{

	public CustomException(String message,Object... args) {
		super(String.format(message, args));
	}
}
