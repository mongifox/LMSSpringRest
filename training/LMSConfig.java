package com.gcit.training;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.gcit.lms.dao.AuthorDAO;
import com.gcit.lms.dao.BookCopiesDAO;
import com.gcit.lms.dao.BookDAO;
import com.gcit.lms.dao.BookLoansDAO;
import com.gcit.lms.dao.BorrowerDAO;
import com.gcit.lms.dao.BranchDAO;
import com.gcit.lms.dao.GenreDAO;
import com.gcit.lms.dao.PublisherDAO;
import com.gcit.lms.service.AdminService;
import com.gcit.lms.service.BorrowerService;
import com.gcit.lms.service.LibrarianService;

@Configuration
public class LMSConfig {
	
	public final String driver = "com.mysql.cj.jdbc.Driver";
	public final String url = "jdbc:mysql://localhost/library";
	public final String username = "root";
	public final String password = "manasa";
	
	@Bean
	public BasicDataSource dataSource(){
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName(driver);
		ds.setUrl(url);
		ds.setUsername(username);
		ds.setPassword(password);
		return ds;
	}
	
	@Bean
	public JdbcTemplate jdbcTemplate(){
		return new JdbcTemplate(dataSource());
	}
	
	@Bean
	public AuthorDAO aDao(){
		return new AuthorDAO();
	}
	
	@Bean
	public PlatformTransactionManager txManager(){
		return new DataSourceTransactionManager(dataSource());
	}
	
	@Bean
	public BookDAO bDao(){
		return new BookDAO();
	}
	
	@Bean
	public BranchDAO brDao(){
		return new BranchDAO();
	}
	
	@Bean
	public BookCopiesDAO bcDao(){
		return new BookCopiesDAO();
	}
	
	@Bean
	public BookLoansDAO blDao(){
		return new BookLoansDAO();
	}
	
	@Bean
	public BorrowerDAO brwDao(){
		return new BorrowerDAO();
	}
	
	@Bean
	public PublisherDAO pDao(){
		return new PublisherDAO();
	}
	
	@Bean
	public GenreDAO gDao(){
		return new GenreDAO();
	}
	
	@Bean
	public AdminService adminService(){
		return new AdminService();
	}
	
	@Bean
	public LibrarianService libService(){
		return new LibrarianService();
	}
	
	@Bean
	public BorrowerService brwService() {
		return new BorrowerService();
	}
	
}
