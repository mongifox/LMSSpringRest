/**
 * 
 */
package com.gcit.lms.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.core.ResultSetExtractor;

import com.gcit.lms.entity.BookLoans;

/**
 * @author tejassrinivas
 *
 */
public class BookLoansDAO extends BaseDAO<BookLoans> implements ResultSetExtractor<List<BookLoans>>  {

	// 1. Add Book Loans
	public void addBookLoans(BookLoans bookLoans)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		jdbcTemplate.update("INSERT INTO tbl_book_loans (bookId, branchId, cardNo, dateOut, dueDate) VALUES (?,?,?,now(),date_add(now(), INTERVAL 1 WEEK))",
				new Object[] { bookLoans.getBookId(), bookLoans.getBranchId(), bookLoans.getCardNo() });
	}
	
	//2.
	public void updateBookLoanDueDate(Integer bookId, Integer branchId, Integer cardNo, Date duedate) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		System.out.println("Entered here override inside dao");
		jdbcTemplate.update("UPDATE tbl_book_loans SET dueDate = ? WHERE bookId = ? and branchId = ? and cardNo = ?", new Object[] {
				duedate, bookId, branchId, cardNo });
		System.out.println(duedate + "--" + bookId + "--" + branchId + "--" + cardNo +"--");
	}
	
	//3.
	public void updateBookLoans(BookLoans bookLoans) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		jdbcTemplate.update("UPDATE tbl_book_loans SET dateIn = now() WHERE bookId = ? and branchId = ? and cardNo = ?",
				new Object[] { bookLoans.getBookId(), bookLoans.getBranchId(), bookLoans.getCardNo() });
	}

	//4.
	public void deleteBookLoan(BookLoans bookLoans)
			throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		jdbcTemplate.update("DELETE FROM tbl_book_loans WHERE bookId = ? and branchId = ? and cardNo = ?",
				new Object[] { bookLoans.getBookId(), bookLoans.getBranchId(), bookLoans.getCardNo() });
	}

	// 5. Read ALL Book Copies
	public List<BookLoans> readAllBookLoans()
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		return jdbcTemplate.query("SELECT * FROM tbl_book_loans", this);
	}

	//6.
	public List<BookLoans> readBookLoansByName(String loanName)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		loanName = "%" + loanName + "%";
		return jdbcTemplate.query("SELECT * FROM tbl_book_loans, tbl_borrower WHERE tbl_borrower.name = ? and tbl_borrower.cardNo = tbl_book_loans.cardNo",new Object[] { loanName }, this);
	}

	//7.
	public List<BookLoans> readBookLoansByDateIn()
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		return jdbcTemplate.query("SELECT * FROM tbl_book_loans where dateIn IS null", this);
	}
	
	//8.
	public void overrideDueDate(BookLoans bookLoans) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		jdbcTemplate.update("UPDATE tbl_book_loans SET dueDate = date_add(dueDate, INTERVAL 1 week) WHERE bookId = ? and branchId = ? and cardNo = ?",
				new Object[] { bookLoans.getBookId(), bookLoans.getBranchId(), bookLoans.getCardNo() }, this);
	}
	
	public Integer loanedBooksByBorrower(BookLoans bookLoans) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		return jdbcTemplate.queryForObject("SELECT COUNT(*) as COUNT FROM tbl_book_loans WHERE bookId = ? AND branchid = ? AND cardNo = ? AND dateIn IS null",
				new Object[] { bookLoans.getBookId(), bookLoans.getBranchId(), bookLoans.getCardNo() }, Integer.class);
	}
	
	
	//9.
	@Override
	public List<BookLoans> extractData(ResultSet rs) throws SQLException {
		List<BookLoans> bookLoans = new ArrayList<>();
		while (rs.next()) {
			BookLoans bookL = new BookLoans();
			bookL.setBookId(rs.getInt("bookId"));
			bookL.setBranchId(rs.getInt("branchId"));
			bookL.setCardNo(rs.getInt("cardNo"));
			bookL.setDateOut(rs.getString("dateOut"));
			bookL.setDueDate(rs.getString("dueDate"));
			bookL.setDateIn(rs.getString("dateIn"));
			bookLoans.add(bookL);
		}
		return bookLoans;
	}
}
