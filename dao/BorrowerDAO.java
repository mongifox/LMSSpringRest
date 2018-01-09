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

import com.gcit.lms.entity.Borrower;

/**
 * @author tejassrinivas
 *
 */
public class BorrowerDAO extends BaseDAO<Borrower> implements ResultSetExtractor<List<Borrower>> {

	// 1. Add Borrowers Without Returning ID
	public void addBorrower(Borrower borrower)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		jdbcTemplate.update("INSERT INTO tbl_borrower (name, address, phone) VALUES (?)",
				new Object[] { borrower.getName(), borrower.getAddress(), borrower.getPhone() });
	}

	// 2. Add Borrowers With Returning ID	
	public Integer addBorrowerWithID(Borrower borrower) throws SQLException {
		KeyHolder holder = new GeneratedKeyHolder();
		final String sql = "INSERT INTO tbl_borrower (name, address, phone) VALUES (?, ?, ?)";
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, borrower.getName());
				ps.setString(2, borrower.getAddress());
				ps.setString(3, borrower.getPhone());
				return ps;
			}
		}, holder);
		return holder.getKey().intValue();
	}

	// 3. Update Borrower
	public void updateBorrowerAllFields(Borrower borrower)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		jdbcTemplate.update("UPDATE tbl_borrower SET name =?, address =?, phone=?  WHERE cardNo = ?",
				new Object[] { borrower.getName(), borrower.getAddress(), borrower.getPhone(), borrower.getCardNo() });
	}

	// 4. Update Borrower Name
	public void updateBorrowerName(Borrower borrower)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		jdbcTemplate.update("UPDATE tbl_borrower SET name =? WHERE cardNo = ?",
				new Object[] { borrower.getName(), borrower.getCardNo() });
	}

	// 5. Update Borrower Address
	public void updateBorrowerAddress(Borrower borrower)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		jdbcTemplate.update("UPDATE tbl_borrower SET address =? WHERE cardNo = ?",
				new Object[] { borrower.getAddress(), borrower.getCardNo() });
	}

	// 6. Update Borrower Phone
	public void updateBorrowerPhone(Borrower borrower)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		jdbcTemplate.update("UPDATE tbl_borrower SET phone =? WHERE cardNo = ?",
				new Object[] { borrower.getPhone(), borrower.getCardNo() });
	}

	// 7. Delete Borrower
	public void deleteBorrower(Borrower borrower)
			throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		jdbcTemplate.update("DELETE FROM tbl_borrower WHERE cardNo = ?", new Object[] { borrower.getCardNo() });
	}

	// 8. Read All Borrower
	public List<Borrower> readAllBorrower()
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		return jdbcTemplate.query("SELECT * FROM tbl_borrower", this);
	}

	// 9. Read Borrower by Names
	public List<Borrower> readBorrowerByName(String borrowerName)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		borrowerName = "%" + borrowerName + "%";
		return jdbcTemplate.query("SELECT * FROM tbl_borrower WHERE name LIKE ?", new Object[] { borrowerName }, this);
	}

	// 10. Read Borrower By PK
	public Borrower readBorrowerByPK(Integer cardNo)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		List<Borrower> borrowers = jdbcTemplate.query("SELECT * FROM tbl_borrower WHERE cardNo  = ?", new Object[] { cardNo }, this);
		if (borrowers != null) {
			return borrowers.get(0);
		} else {
			return null;
		}
	}

	public boolean checkValidation(Borrower borrower) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		List<Borrower> brVal = jdbcTemplate.query("SELECT * FROM tbl_borrower WHERE cardNo  = ?", new Object[] { borrower.getCardNo()}, this);
		System.out.println("Entering into BorroweDAO validation");
		if(!brVal.isEmpty()) {
			return true;
		}
		return false;
	}
	
	// 11. Extract Data
	@Override
	public List<Borrower> extractData(ResultSet rs) throws SQLException {
		
		List<Borrower> borrower = new ArrayList<>();
		while (rs.next()) {
			Borrower br = new Borrower();
			br.setCardNo(rs.getInt("cardNo"));
			br.setName(rs.getString("name"));
			br.setAddress(rs.getString("address"));
			br.setPhone(rs.getString("phone"));
			borrower.add(br);
		}
		return borrower;
	}
}
