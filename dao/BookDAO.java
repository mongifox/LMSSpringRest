package com.gcit.lms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;


import com.gcit.lms.entity.Author;
import com.gcit.lms.entity.Book;
import com.gcit.lms.entity.Borrower;
import com.gcit.lms.entity.Branch;
import com.gcit.lms.entity.Genre;
import com.gcit.lms.entity.Publisher;

public class BookDAO extends BaseDAO<Book> implements ResultSetExtractor<List<Book>>  {

	// 1. Add Book without returning its ID
	public void addBook(Book book)
			throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		jdbcTemplate.update("INSERT INTO tbl_book (title) VALUES (?)", new Object[] { book.getTitle() });
	}
	
	// 2. Add Book with return its ID
	public Integer addBookWithID(Book book) throws SQLException {
		KeyHolder holder = new GeneratedKeyHolder();
		final String sql = "INSERT INTO tbl_book (title) VALUES (?)";
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, book.getTitle());
				return ps;
			}
		}, holder);
		return holder.getKey().intValue();
	}

	// 3. Add Book and authors entry in book_author table
	public void addBookAuthors(Book book)
			throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		for (Author a : book.getAuthors()) {
			jdbcTemplate.update("INSERT INTO tbl_book_authors VALUES (?, ?)", new Object[] { book.getBookId(), a.getAuthorId() });
		}
	}

	// 4. Update Book title using bookID 
	public void updateBook(Book book)
			throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		jdbcTemplate.update("UPDATE tbl_book SET title =? WHERE bookId = ?", new Object[] { book.getTitle(), book.getBookId() });
	}

	// 5. Update the book id and its corresponding genre ID in books_genre table
	public void addBookWithGenre(Book book)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		for (Genre g : book.getGenres()) {
			jdbcTemplate.update("INSERT INTO tbl_book_genres VALUES (?, ?)", new Object[] { g.getGenreId(), book.getBookId() });
		}
	}

	// 6. Adding an entry with publisher in Book table and returning ID	
	public Integer addBookWithPublisherByID(Book book) throws SQLException {
		KeyHolder holder = new GeneratedKeyHolder();
		final String sql = "INSERT INTO tbl_book (title, pubId) VALUES (?, ?)";
		Publisher pb = book.getPublisher();
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, book.getTitle());
				ps.setInt(2, pb.getPublisherId());
				return ps;
			}
		}, holder);
		return holder.getKey().intValue();
	}

	public void addBookPublisher(Book book, Integer publisherId) throws SQLException {
		jdbcTemplate.update("UPDATE tbl_book SET title =? WHERE pubId = ?", new Object[] { book.getTitle(), publisherId });
	}
	
	public void deleteBook(Book book)
			throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		jdbcTemplate.update("DELETE FROM tbl_book WHERE bookId = ?", new Object[] { book.getBookId() });
	}

	// 5. Delete Book Author
	public void deleteBookAuthor(Book book)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		jdbcTemplate.update("DELETE FROM tbl_book_authors WHERE bookId = ?", new Object[] { book.getBookId() });
	}

	// 6. Read All Books
	public List<Book> readAllBooks()
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		System.out.println("Entering inside read books DAO module");
		return jdbcTemplate.query("SELECT * FROM tbl_book", this);
	}

	// 7. Read All Books by Name
	public List<Book> readBooksByName(String title)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		title = "%" + title + "%";
		 return jdbcTemplate.query("SELECT * FROM tbl_book WHERE title LIKE ?", new Object[] { title }, this);
	}
	
	// 8. Read All books by PK
	public Book readBookByPK(Integer bookId)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		List<Book> books =  jdbcTemplate.query("SELECT * FROM tbl_book WHERE bookId  = ?", new Object[] { bookId }, this);
		if (books != null) {
			return books.get(0);
		} else {
			return null;
		}
	}
	
	//9. Check out for selecting total books in that branch
	public List<Book> readBooksByBranches(Branch branch)
			throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		return jdbcTemplate.query("SELECT * FROM tbl_book WHERE bookId IN (SELECT bookId FROM tbl_book_copies WHERE branchId = ?)",
				new Object[] { branch.getBranchId() }, this);
	}

	//10.  Return for displaying total books taken by that borrower
	public List<Book> readBooksByBorrowers(Branch branch, Borrower borrower) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		return jdbcTemplate.query("SELECT * FROM tbl_book WHERE bookId IN (SELECT bookId FROM tbl_book_loans WHERE branchId = ? AND cardNo = ? AND dateIn IS null)",
				new Object[] { branch.getBranchId(), borrower.getCardNo() }, this);
	}
	
	public List<Book> readBooksByAuthor(Author author) throws SQLException{
		return jdbcTemplate.query("SELECT * FROM tbl_book WHERE bookId IN (SELECT bookId FROM tbl_book_authors WHERE authorId=?)", new Object[] {author.getAuthorId()}, this);
	}
	
	public List<Book> readBooksByPublisher(Publisher publisher) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		return jdbcTemplate.query("SELECT * from tbl_book WHERE pubId = ?", new Object[] { publisher.getPublisherId() }, this);
	}
	
	//11. Read All Authors by Page Number
	public List<Book> readAllBooksByPg(Integer pageNo) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		setPageNo(pageNo);
		System.out.println("Page No inside book dao" + pageNo);
		return jdbcTemplate.query(limitFunc("SELECT * FROM tbl_book"), this);
	}

	//12. Get Books Count
	public Integer getBooksCount() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		return jdbcTemplate.queryForObject("SELECT COUNT(*) AS COUNT FROM tbl_book", Integer.class);
	}

	// 13. Extract Data
	@Override
	public List<Book> extractData(ResultSet rs) throws SQLException {
		List<Book> books = new ArrayList<>();
		while (rs.next()) {
			Book b = new Book();
			b.setBookId(rs.getInt("bookId"));
			b.setTitle(rs.getString("title"));
			books.add(b);
		}
		return books;
	}

}
