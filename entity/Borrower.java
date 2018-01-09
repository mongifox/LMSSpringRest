package com.gcit.lms.entity;

import java.io.Serializable;
import java.util.List;

public class Borrower implements Serializable {

	private static final long serialVersionUID = -7540065783735279520L;
	
	private Integer cardNo;
	private String name;
	private String address;
	private String phone;
	private List<Branch> branches;
	private List<Book> books;
	private List<BookLoans> loans;

	/*
	 * hashcode for cardNo and name
	 */
	
	
	public List<BookLoans> getLoans() {
		return loans;
	}

	public void setLoans(List<BookLoans> loans) {
		this.loans = loans;
	}

	public Integer getCardNo() {
		return cardNo;
	}

	public void setCardNo(Integer cardNo) {
		this.cardNo = cardNo;
	}

	public List<Branch> getBranches() {
		return branches;
	}

	public void setBranches(List<Branch> branches) {
		this.branches = branches;
	}

	public List<Book> getBooks() {
		return books;
	}

	public void setBooks(List<Book> books) {
		this.books = books;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

}
