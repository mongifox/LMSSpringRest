package com.gcit.lms.entity;

import java.io.Serializable;
import java.util.List;

public class Branch implements Serializable {

	private static final long serialVersionUID = 5321876945492947451L;
	private Integer branchId;
	private String branchName;
	private String branchAddress;
	private List<BookLoans> loans; //not needed
	private List<Borrower> borrower; //not needed
	private List<Book> books; // to display the all books present in this branch
	private List<BookCopies> bookCopies; // just to be on a safer side (maybe need in futures)
	
	public List<BookLoans> getLoans() {
		return loans;
	}

	public void setLoans(List<BookLoans> loans) {
		this.loans = loans;
	}

	public List<Borrower> getBorrower() {
		return borrower;
	}

	public void setBorrower(List<Borrower> borrower) {
		this.borrower = borrower;
	}

	public List<BookCopies> getBookCopies() {
		return bookCopies;
	}

	public void setBookCopies(List<BookCopies> list) {
		this.bookCopies = list;
	}

	public Integer getBranchId() {
		return branchId;
	}

	public void setBranchId(Integer branchId) {
		this.branchId = branchId;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getBranchAddress() {
		return branchAddress;
	}

	public void setBranchAddress(String branchAddress) {
		this.branchAddress = branchAddress;
	}

	public List<Book> getBooks() {
		return books;
	}

	public void setBooks(List<Book> books) {
		this.books = books;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((branchId == null) ? 0 : branchId.hashCode());
		result = prime * result + ((branchName == null) ? 0 : branchName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Branch other = (Branch) obj;
		if (branchId == null) {
			if (other.branchId != null)
				return false;
		} else if (!branchId.equals(other.branchId))
			return false;
		if (branchName == null) {
			if (other.branchName != null)
				return false;
		} else if (!branchName.equals(other.branchName))
			return false;
		return true;
	}

}
