package com.gcit.lms.entity;

import java.util.List;

import com.gcit.lms.dao.BookCopiesDAO;

public class Book {
	
	private Integer bookId;
	private String title;
	private Publisher publisher;
	private Integer noOfCopiesAvailable;
	private Integer branchIdNew;
	public Integer getNoOfCopiesAvailable() {
		return noOfCopiesAvailable;
	}
	public void setNoOfCopiesAvailable(Integer noOfCopiesAvailable) {
		this.noOfCopiesAvailable = noOfCopiesAvailable;
	}
	private List<Author> authors;
	private List<Genre> genres;
	private List<Branch> branches;
	private List<BookCopies> bookCopies; //to get the total copies of those books
	
	/*
	 *  create hashcode for bookId and title
	 */
	
	public List<BookCopies> getBookCopies() {
		return bookCopies;
	}
	public void setBookCopies(List<BookCopies> list) {
		this.bookCopies = list;
	}
	/**
	 * @return the bookId
	 */
	public Integer getBookId() {
		return bookId;
	}
	public List<Branch> getBranches() {
		return branches;
	}
	public void setBranches(List<Branch> branches) {
		this.branches = branches;
	}
	/**
	 * @param bookId the bookId to set
	 */
	public void setBookId(Integer bookId) {
		this.bookId = bookId;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the publisher
	 */
	public Publisher getPublisher() {
		return publisher;
	}
	/**
	 * @param publisher the publisher to set
	 */
	public void setPublisher(Publisher publisher) {
		this.publisher = publisher;
	}
	/**
	 * @return the authors
	 */
	public List<Author> getAuthors() {
		return authors;
	}
	/**
	 * @param authors the authors to set
	 */
	public void setAuthors(List<Author> authors) {
		this.authors = authors;
	}
	/**
	 * @return the genres
	 */
	public List<Genre> getGenres() {
		return genres;
	}
	/**
	 * @param genres the genres to set
	 */
	public void setGenres(List<Genre> genres) {
		this.genres = genres;
	}
	public Integer getBranchIdNew() {
		return branchIdNew;
	}
	public void setBranchIdNew(Integer branchIdNew) {
		this.branchIdNew = branchIdNew;
	}

}
