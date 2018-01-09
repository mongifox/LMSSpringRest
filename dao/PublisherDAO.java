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

import com.gcit.lms.entity.Publisher;

public class PublisherDAO extends BaseDAO<Publisher> implements ResultSetExtractor<List<Publisher>> {

	// 1. Add Publisher Without Returning ID
	public void addPublisher(Publisher publisher)
			throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		jdbcTemplate.update("INSERT INTO tbl_publisher (publisherName, publisherAddress, publisherPhone) VALUES (?, ?, ?)",
				new Object[] { publisher.getPublisherName(), publisher.getPublisherAddress(),
						publisher.getPublisherPhone() });
	}

	// 2. Add Publisher With Returning ID
	public Integer addPublisherWithID(Publisher publisher) throws SQLException {
		KeyHolder holder = new GeneratedKeyHolder();
		final String sql = "INSERT INTO tbl_publisher (publisherName, publisherAddress, publisherPhone) VALUES (?, ?, ?)";
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, publisher.getPublisherName());
				ps.setString(2, publisher.getPublisherAddress());
				ps.setString(3, publisher.getPublisherPhone());
				return ps;
			}
		}, holder);
		return holder.getKey().intValue();
	}
	
	// 3. Update Publisher
	public void updatePublisher(Publisher publisher)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		jdbcTemplate.update("UPDATE tbl_publisher SET publisherName = ? , publisherAddress = ?, publisherPhone = ? WHERE publisherId = ?",
				new Object[] { publisher.getPublisherName(), publisher.getPublisherAddress(),
						publisher.getPublisherPhone(), publisher.getPublisherId() });
	}

	// 4. Delete Publisher
	public void deletePublisher(Publisher publisher)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		jdbcTemplate.update("DELETE FROM tbl_publisher WHERE publisherId = ?", new Object[] { publisher.getPublisherId() });
	}

	// 5. Read All Publishers
	public List<Publisher> readAllPublisher()
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		return jdbcTemplate.query("SELECT * FROM tbl_publisher", this);
	}

	// 6. Read All Publishers by Names
	public List<Publisher> readPublishersByName(String publisherName)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		publisherName = "%" + publisherName + "%";
		return jdbcTemplate.query("SELECT * FROM tbl_publisher WHERE publisherName LIKE ?", new Object[] { publisherName }, this);
	}

	// 7. Read All Publishers by ID and Return Publisher
	public Publisher readPublisherByPK(Integer pk)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		List<Publisher> publishers = jdbcTemplate.query("SELECT * FROM tbl_publisher WHERE publisherId  = ?", new Object[] { pk }, this);
		if (publishers != null) {
			return publishers.get(0);
		} else {
			return null;
		}
	}

	// 8. Extract Data
	@Override
	public List<Publisher> extractData(ResultSet rs) throws SQLException {
		List<Publisher> publishers = new ArrayList<>();
		while (rs.next()) {
			Publisher p = new Publisher();
			p.setPublisherId(rs.getInt("publisherId"));
			p.setPublisherName(rs.getString("publisherName"));
			p.setPublisherAddress(rs.getString("publisherAddress"));
			p.setPublisherPhone(rs.getString("publisherPhone"));
			publishers.add(p);
		}
		return publishers;
	}
}
