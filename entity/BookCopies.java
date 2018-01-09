package com.gcit.lms.entity;

public class BookCopies {

	/*
	 * Particular Branch How many Books are there and how many copies of that
	 * particular branch are there
	 */

	// these entities are more than enough

	// private Integer bookId;
	// private Integer branchId;
	private Integer noOfCopies;
	private Integer bookId;
	private Integer branchId;


	public Integer getBookId() {
		return bookId;
	}

	public void setBookId(Integer bookId) {
		this.bookId = bookId;
	}

	public Integer getBranchId() {
		return branchId;
	}

	public void setBranchId(Integer branchId) {
		this.branchId = branchId;
	}

	public Integer getNoOfCopies() {
		return noOfCopies;
	}

	public void setNoOfCopies(Integer noOfCopies) {
		this.noOfCopies = noOfCopies;
	}

	

}
