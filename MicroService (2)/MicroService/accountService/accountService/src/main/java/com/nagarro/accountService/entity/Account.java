package com.nagarro.accountService.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int act_id;
	private String accountNo;
	private double balance;
	private int customerId;
	private LocalDate createdAt;
	private LocalDate updatedAt;
//	public LocalDate getCreatedAt() {
//		return createdAt;
//	}
//	public void setCreatedAt(LocalDate createdAt) {
//		this.createdAt = createdAt;
//	}
//	public LocalDate getUpdatedAt() {
//		return updatedAt;
//	}
//	public void setUpdatedAt(LocalDate updatedAt) {
//		this.updatedAt = updatedAt;
//	}
//	public int getAct_id() {
//		return act_id;
//	}
//	public void setAct_id(int act_id) {
//		this.act_id = act_id;
//	}
//	public String getAccountNo() {
//		return accountNo;
//	}
//	public void setAccountNo(String accountNo) {
//		this.accountNo = accountNo;
//	}
//	public double getBalance() {
//		return balance;
//	}
//	public void setBalance(double balance) {
//		this.balance = balance;
//	}
//	
}
