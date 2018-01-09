/**
 * 
 */
package com.gcit.lms.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.gcit.lms.dao.BookCopiesDAO;
import com.gcit.lms.dao.BookDAO;
import com.gcit.lms.dao.BranchDAO;
import com.gcit.lms.entity.Book;
import com.gcit.lms.entity.BookCopies;
import com.gcit.lms.entity.Branch;

/**
 * @author tejassrinivas
 *
 */

public class LibrarianService {
		
	@Autowired
	BranchDAO brDao;
	
	@Autowired
	BookDAO bDao;
	
	@Autowired
	BookCopiesDAO bcDao;
	
	@RequestMapping(value = "/viewAllBranches", method = RequestMethod.GET, produces="application/json")
	public List<Branch> readBranches() {
		System.out.println("Entering here inside admin br");
		List<Branch> branches = null;
		try {
			branches = brDao.readAllBranches();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return branches;
	}
	
	public Branch readBranchByPk(Integer branchId) {
		System.out.println("Entering once Branch by PK with id : " + branchId);
		Branch branch = null;
		try {
			branch =  brDao.readBranchByPK(branchId);
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return branch;
	}
	
	@RequestMapping(value = "/viewBooksByBranch", method = RequestMethod.POST, consumes="application/json", produces="application/json")
	public List<Book> readBooksByBranch(@RequestBody Branch branch)  {
		List<Book> books = null;
		try {
			books = bDao.readBooksByBranches(branch);
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return books;
	}
	
	@RequestMapping(value = "/viewBookCountInBranch/{branchId}/{bookId}", method = RequestMethod.GET, produces="application/json")
	public Integer readBookCountInBranch(@PathVariable Integer branchId,@PathVariable  Integer bookId) {
		System.out.println("Entering here Book Count In branch Once : " + branchId +" book : " + bookId);
		BookCopies bookCopies = bcDao.readBookCopiesByBothIds(branchId, bookId);
		System.out.println("Successfully returning back from readBookCopiesByBothIds");
		Integer noOfCopies = 0;
		try {
			noOfCopies = bcDao.readTotalBookCopies(bookCopies);
			System.out.println("No Of Copies in Librarian Service page: " + noOfCopies);
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return noOfCopies;
	}
	
	@RequestMapping(value = "/saveBookCopies", method = RequestMethod.POST, consumes="application/json", produces="application/json")
	@Transactional
	public void saveBookCopies(@RequestBody Integer noOfCopies,@RequestBody Integer bookId,@RequestBody  Integer branchId) {
		System.out.println("Entering save copies in librarian service");
		BookCopies bookCopies = new BookCopies();
		bookCopies.setNoOfCopies(noOfCopies);
		bookCopies.setBookId(bookId);
		bookCopies.setBranchId(branchId);
		System.out.println("Book Copie after setting it : " + bookCopies.getNoOfCopies());
		try {
			bcDao.updateCopies(bookCopies);
			System.out.println("leaving save copies in librarian service");
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/updateBranch", method = RequestMethod.POST, consumes="application/json", produces="application/json")
	public void updateBranch(@RequestBody String branchName,@RequestBody  String branchAddress,@RequestBody  Integer branchId) {
		Branch branch = new Branch();
		branch.setBranchName(branchName);
		branch.setBranchAddress(branchAddress);
		branch.setBranchId(branchId);
		try {
			brDao.updateBranch(branch);
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
}
