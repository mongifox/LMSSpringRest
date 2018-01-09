package com.gcit.lms.entity;

import java.io.Serializable;

//import java.util.List;

public class BookLoans implements Serializable {

	private static final long serialVersionUID = -3865738337707787783L;
	// private List<Book> books;
	// private List<Branch> branches;
	// private List<Borrower> borrowers;

//	private Book books;
//	private Branch branch;
//	private Borrower borrower;
	private Integer bookId;
	private Integer cardNo;
	private Integer branchId;
	private String dateOut;
	private String dateIn;
	private String dueDate;
	
	
	public Integer getBookId() {
		return bookId;
	}

	public void setBookId(Integer bookId) {
		this.bookId = bookId;
	}

	public Integer getCardNo() {
		return cardNo;
	}

	public void setCardNo(Integer cardNo) {
		this.cardNo = cardNo;
	}

	public Integer getBranchId() {
		return branchId;
	}

	public void setBranchId(Integer branchId) {
		this.branchId = branchId;
	}



	public String getDateOut() {
		return dateOut;
	}

	public void setDateOut(String dateOut) {
		this.dateOut = dateOut;
	}

	public String getDateIn() {
		return dateIn;
	}

	public void setDateIn(String dateIn) {
		this.dateIn = dateIn;
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

}
