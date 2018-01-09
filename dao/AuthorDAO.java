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

public class AuthorDAO extends BaseDAO<Author> implements ResultSetExtractor<List<Author>> {

	// 1. Add Author Without Returning ID
	public void addAuthor(Author author)
			throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		jdbcTemplate.update("INSERT INTO tbl_author (authorName) VALUES (?)", new Object[] { author.getAuthorName() });
	}

	// 2. Add Author With Returning ID
	public Integer addAuthorWithID(Author author) throws SQLException {
		KeyHolder holder = new GeneratedKeyHolder();
		final String sql = "INSERT INTO tbl_author (authorName) VALUES (?)";
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, author.getAuthorName());
				return ps;
			}
		}, holder);
		return holder.getKey().intValue();
	}

	//3. Add Author to Book Author Table
	public void addAuthorBooks(Author author)
			throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		for (Book b : author.getBooks()) {
			jdbcTemplate.update("INSERT INTO tbl_book_authors VALUES (?, ?)", new Object[] { b.getBookId(), author.getAuthorId() });
		}
	}
	
	// 4. Update Author
	public void updateAuthor(Author author)
			throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		jdbcTemplate.update("UPDATE tbl_author SET authorName =? WHERE authorId = ?",
				new Object[] { author.getAuthorName(), author.getAuthorId() });
	}

	// 5. Delete Author
	public void deleteAuthor(Author author)
			throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		jdbcTemplate.update("DELETE FROM tbl_author WHERE authorId = ?", new Object[] { author.getAuthorId() });
	}

	public void deleteAuthorBook(Author author)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		jdbcTemplate.update("DELETE FROM tbl_book_authors WHERE authorId = ?", new Object[] { author.getAuthorId() });
	}
	
	public List<Author> readAuthorByBooks(Book book) throws SQLException{
		return jdbcTemplate.query("SELECT * FROM tbl_author WHERE authorId IN (SELECT authorId FROM tbl_book_authors WHERE bookId = ?)", new Object[] {book.getBookId()}, this);
	}
	
	// 7. Read All Authors
	public List<Author> readAllAuthors()
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		return jdbcTemplate.query("SELECT * FROM tbl_author", this);
	}
	
	// Read All Authors by Page Number
	public List<Author> readAllAuthorsByPg(Integer pageNo)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		setPageNo(pageNo);
		System.out.println("Page No inside dao : " + pageNo);
		return jdbcTemplate.query(limitFunc("SELECT * FROM tbl_author"), this);
	}
	
	
	public Integer getAuthorsCount()
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		return jdbcTemplate.queryForObject("SELECT COUNT(*) AS COUNT FROM tbl_author", Integer.class);
	}
	
	// 8. Read All Authors by Names
	public List<Author> readAuthorsByName(String authorName)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		authorName = "%" + authorName + "%";
		return jdbcTemplate.query("SELECT * FROM tbl_author WHERE authorName LIKE ?", new Object[] { authorName }, this);
	}

	// 9. Read All Authors by ID and return Author
	public Author readAuthorByPK(Integer authorId)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		List<Author> authors = jdbcTemplate.query("SELECT * FROM tbl_author WHERE authorId  = ?", new Object[] { authorId }, this);
		if (authors != null) {
			return authors.get(0);
		} else {
			return null;
		}
	}
	
	public List<Author> readAuthorsByBook(Integer bookId) throws SQLException{
		return jdbcTemplate.query("SELECT * FROM tbl_author WHERE authorId IN (SELECT authorId FROM tbl_book_authors WHERE bookId = ?)", new Object[] {bookId}, this);
	}
	
	//10. Extract Data
	@Override
	public List<Author> extractData(ResultSet rs)
			throws SQLException {
		List<Author> authors = new ArrayList<>();
		while (rs.next()) {
			Author a = new Author();
			a.setAuthorId(rs.getInt("authorId"));
			a.setAuthorName(rs.getString("authorName"));
			authors.add(a);
		}
		return authors;
	}

	
}
