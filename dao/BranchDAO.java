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
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.gcit.lms.entity.Branch;

/**
 * @author tejassrinivas
 *
 */
@Repository
public class BranchDAO extends BaseDAO<Branch> implements ResultSetExtractor<List<Branch>> {

	public void addBranch(Branch branch)
			throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		jdbcTemplate.update("INSERT INTO tbl_library_branch (branchName, branchAddress) VALUES (?,?)", new Object[] { branch.getBranchName(),branch.getBranchAddress()  });
	}

	public Integer addBranchWithID(Branch branch) throws SQLException {
		KeyHolder holder = new GeneratedKeyHolder();
		final String sql = "INSERT INTO tbl_library_branch (branchName, branchAddress) VALUES (?,?)";
		//Publisher pb = book.getPublisher();
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, branch.getBranchName());
				ps.setString(2, branch.getBranchAddress());
				return ps;
			}
		}, holder);
		return holder.getKey().intValue();
	}
	
	public void updateBranch(Branch branch) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		jdbcTemplate.update("UPDATE tbl_library_branch SET branchName =?, branchAddress = ? WHERE branchId = ?", new Object[] { branch.getBranchName(),branch.getBranchAddress(), branch.getBranchId() });
	}
	
	public void deleteBranch(Branch branch)
			throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		jdbcTemplate.update("DELETE FROM tbl_library_branch WHERE branchId = ?", new Object[] { branch.getBranchId() });
	}
	
	public List<Branch> readAllBranches() throws SQLException {
		System.out.println("Branch DAO");
		return jdbcTemplate.query("SELECT * FROM tbl_library_branch", this);
	}
	
	public List<Branch> readBranchesByName(String branchName)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		branchName = "%" + branchName + "%";
		return jdbcTemplate.query("SELECT * FROM tbl_library_branch WHERE branchName LIKE ?", new Object[] { branchName }, this);
	}
	
	public Branch readBranchByPK(Integer branchId)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		List<Branch> branches = jdbcTemplate.query("SELECT * FROM tbl_library_branch WHERE branchId  = ?", new Object[] { branchId }, this);
		if (branches != null) {
			System.out.println("Inside BranchDAO once");
			if(branches.get(0)!=null) {
				System.out.println("Inside BranchDAO twice");
				return branches.get(0);
			}
		} else {
			return null;
		}
		return null;
	}
	
	@Override
	public List<Branch> extractData(ResultSet rs) throws SQLException {
		List<Branch> branches = new ArrayList<>();
		while(rs.next()) {
			Branch br = new Branch();
			br.setBranchId(rs.getInt("branchId"));
			br.setBranchName(rs.getString("branchName"));
			br.setBranchAddress(rs.getString("branchAddress"));
			branches.add(br);
		}
		return branches;
	}
}
