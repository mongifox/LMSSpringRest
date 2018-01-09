/**
 * 
 */
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
import com.gcit.lms.entity.BookCopies;
import com.gcit.lms.entity.Genre;

/**
 * @author tejassrinivas
 *
 */
public class GenreDAO extends BaseDAO<Genre> implements ResultSetExtractor<List<Genre>>{

	public void addGenre(Genre genre)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		jdbcTemplate.update("INSERT INTO tbl_genre (genre_name) VALUES (?)", new Object[] { genre.getGenreName() });
	}

	public Integer addGenreWithID(Genre genre) throws SQLException {
		KeyHolder holder = new GeneratedKeyHolder();
		final String sql = "INSERT INTO tbl_genre (genre_name) VALUES (?)";
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, genre.getGenreName() );
				return ps;
			}
		}, holder);
		return holder.getKey().intValue();
	}
	
	
	public void addBookGenre(Genre genre)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		for (Book b : genre.getBooks()) {
			jdbcTemplate.update("INSERT INTO tbl_book_genres (genre_id, bookId) VALUES (?, ?)",
					new Object[] { genre.getGenreId(), b.getBookId() });
		}
	}

	public void updateGenre(Genre genre)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		jdbcTemplate.update("UPDATE tbl_genre SET genre_name =? WHERE genre_id = ?",
				new Object[] { genre.getGenreName(), genre.getGenreId() });
	}

	public void deleteGenre(Genre genre)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		jdbcTemplate.update("DELETE FROM tbl_genre WHERE genre_id = ?", new Object[] { genre.getGenreId() });
	}

	//delete genre in book_genres table also
	public void deleteBookGenre(Genre genre) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		jdbcTemplate.update("DELETE FROM tbl_book_genres WHERE genre_id = ?", new Object[] {genre.getGenreId()});
	}
	
	public List<Genre> readAllGenre()
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		return jdbcTemplate.query("SELECT * FROM tbl_genre", this);
	}

	public List<Genre> readGenresByBook(Book book) throws SQLException{
		return jdbcTemplate.query("SELECT * FROM tbl_genre WHERE genre_id IN (SELECT genre_id FROM tbl_book_genres WHERE bookId = ?)", new Object[] {book.getBookId()}, this);
	}
	
	//read genre by name
	public List<Genre> readGenreByName(String genre_name)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		genre_name = "%" + genre_name + "%";
		return jdbcTemplate.query("SELECT * FROM tbl_genre WHERE genre_name LIKE ?", new Object[] { genre_name }, this);
	}
	
	//read genre by PK
	public Genre readGenreByPK(Integer genre_id)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		List<Genre> genres = jdbcTemplate.query("SELECT * FROM tbl_genre WHERE genre_id  = ?", new Object[] { genre_id }, this);
		if (genres != null) {
			return genres.get(0);
		} else {
			return null;
		}
	}
	
	@Override
	public List<Genre> extractData(ResultSet rs) throws SQLException {
		List<Genre> gens = new ArrayList<>();
		while (rs.next()) {
			Genre g = new Genre();
			g.setGenreId(rs.getInt("genre_id"));
			g.setGenreName(rs.getString("genre_name"));
			gens.add(g);
		}
		return gens;
	}

}
