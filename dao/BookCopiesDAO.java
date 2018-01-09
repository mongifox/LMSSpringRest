package com.gcit.lms.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.ResultSetExtractor;

import com.gcit.lms.entity.BookCopies;

/**
 * @author tejassrinivas
 *
 */
public class BookCopiesDAO extends BaseDAO<BookCopies> implements ResultSetExtractor<List<BookCopies>> {

	// 1. Add
	public void addBookCopies(BookCopies bookCopies)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		jdbcTemplate.update("INSERT INTO tbl_book_copies (bookId, branchId, noOfCopies) VALUES (?, ?, ?)",
				new Object[] { bookCopies.getBookId(), bookCopies.getBranchId(),
						bookCopies.getNoOfCopies() });
	}

	// 2. Update
	public void updateCopies(BookCopies bookCopies)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		jdbcTemplate.update("UPDATE tbl_book_copies SET noOfCopies =? WHERE bookId = ? AND branchId = ?",
				new Object[] {bookCopies.getNoOfCopies(), bookCopies.getBookId(), bookCopies.getBranchId() });
	}
	
	//3. Updating copies when borrower checks out a book
	public void updateBookCopies(BookCopies bookCopies) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		jdbcTemplate.update("UPDATE tbl_book_copies SET noOfCopies = noOfCopies-1 WHERE bookId = ? and branchID = ?",
				new Object[] { bookCopies.getBookId(), bookCopies.getBranchId() });		
	}

	//4. Updating copies when borrower returns a book
	public void updateLoanBookCopies(BookCopies bookCopies) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		jdbcTemplate.update("UPDATE tbl_book_copies SET noOfCopies = noOfCopies+1 WHERE bookId = ? and branchID = ?",
				new Object[] { bookCopies.getBookId(), bookCopies.getBranchId() });		
	}
	
	// 5. Delete
	public void deleteBookCopies(BookCopies bookCopies) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		jdbcTemplate.update("DELETE FROM tbl_book_copies WHERE bookId = ? AND branchId = ?", new Object[] { bookCopies.getBookId(), bookCopies.getBranchId() });
	}

	// 6. Read ALL Book Copies
	public List<BookCopies> readAllBookCopies()
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		return jdbcTemplate.query("SELECT * FROM tbl_book_copies", this);
	}

	//7. Read Book Copy for a particular entry
	public BookCopies readBookCopies(BookCopies bookCopy)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		List<BookCopies> bc = jdbcTemplate.query("SELECT * FROM tbl_book_copies WHERE bookId = ? AND branchId = ?",
				new Object[] { bookCopy.getBookId(), bookCopy.getBranchId() }, this);
		if (bc != null) {
			return bc.get(0);
		} else {
			return null;
		}
	}

	// 8. ReadWithPK (Working correctly?...Check in the testing)
	public BookCopies readBookCopiesWithPK(Integer bookId, Integer branchId) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		List<BookCopies> bookCopies = jdbcTemplate.query("SELECT noOfCopies FROM tbl_book_copies WHERE bookId  = ? AND branchId = ?", new Object[] { bookId, branchId } , this);
		if (bookCopies != null) {
			return bookCopies.get(0);
		} else {
			return null;
		}
	}
	
	public List<BookCopies> readBookCopieForaBook(Integer bookId) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		return	jdbcTemplate.query("SELECT * FROM tbl_book_copies WHERE bookId  = ? ", new Object[] { bookId }, this);
	}
	
	//9. Read Book Copies using both branch and book id's
	public BookCopies readBookCopiesByBothIds( Integer branchId, Integer bookId) {
		BookCopies bc = new BookCopies();
		bc.setBranchId(branchId);
		bc.setBookId(bookId);
		return bc;
	}
	
	public Integer readTotalBookCopies(BookCopies bookCopies) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		List<BookCopies> bc = jdbcTemplate.query("SELECT * FROM tbl_book_copies WHERE branchId = ? AND bookId = ?", new Object[] { bookCopies.getBranchId(), bookCopies.getBookId() }, this) ;
		if(bc!=null) {
			System.out.println("Entering here inside Total Book Copies");
			BookCopies b = bc.get(0);
			
			System.out.println("No of Copies inside Read Total Book Copies : " + b.getNoOfCopies());
			return b.getNoOfCopies();
		}
		return null;
	}

	@Override
	public List<BookCopies> extractData(ResultSet rs) throws SQLException {
		List<BookCopies> bookCopies = new ArrayList<>();
		while (rs.next()) {
			BookCopies bc = new BookCopies();
			bc.setBookId(rs.getInt("bookId"));
			bc.setBranchId(rs.getInt("branchId"));
			bc.setNoOfCopies(rs.getInt("noOfCopies"));
			bookCopies.add(bc);
		}
		return bookCopies;
	}
}
