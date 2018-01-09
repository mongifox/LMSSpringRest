package com.gcit.lms.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public abstract class BaseDAO<T> {
	
	@Autowired
	public JdbcTemplate jdbcTemplate;
	
	private Integer pageNo = 0;
	private Integer pageSize = 8;
	
	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public String limitFunc(String sql){
		if(getPageNo() > 0){
			return sql+= " LIMIT "+(getPageNo()-1)*8+" ,"+getPageSize();
		}
		return null;
	}
	
}
