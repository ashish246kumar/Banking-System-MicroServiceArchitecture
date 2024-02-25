package com.nagarro.accountService.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


import com.nagarro.accountService.entity.Account;

public interface AccountRepositoy extends JpaRepository<Account,Integer>{



	Account findByCustomerId(int customerId);

	

}
