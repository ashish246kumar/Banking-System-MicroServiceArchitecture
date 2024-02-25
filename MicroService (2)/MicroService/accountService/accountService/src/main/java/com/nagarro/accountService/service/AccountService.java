package com.nagarro.accountService.service;

import com.nagarro.accountService.dto.AccountDtoResponse;
import com.nagarro.accountService.dto.AcountDtoRequest;
import com.nagarro.accountService.entity.Account;
import com.nagarro.accountService.exception.CustomException;

public interface AccountService {

	String addMoney(AcountDtoRequest acountDtoRequest) throws CustomException;

	public void withDrawMoney(int actId,double balance) throws CustomException;
	
	public AccountDtoResponse accountInfo(int actId) throws CustomException;
	
	public void deleteAccountByCustomerId(int customerId) throws CustomException;
	
	public void deleteAccountByActId(int actId) throws CustomException;
	

}
