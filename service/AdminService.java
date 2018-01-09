package com.gcit.lms.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gcit.lms.dao.AuthorDAO;
import com.gcit.lms.dao.BookCopiesDAO;
import com.gcit.lms.dao.BookDAO;
import com.gcit.lms.dao.BookLoansDAO;
import com.gcit.lms.dao.BorrowerDAO;
import com.gcit.lms.dao.BranchDAO;
import com.gcit.lms.dao.GenreDAO;
import com.gcit.lms.dao.PublisherDAO;
import com.gcit.lms.entity.Author;
import com.gcit.lms.entity.Book;
import com.gcit.lms.entity.BookLoans;
import com.gcit.lms.entity.Borrower;
import com.gcit.lms.entity.Branch;
import com.gcit.lms.entity.Genre;
import com.gcit.lms.entity.Publisher;

@RestController
public class AdminService {
	
	@Autowired 
	AuthorDAO aDao;
	
	@Autowired
	BookDAO bDao;
	
	@Autowired
	BranchDAO brDao;
	
	@Autowired
	PublisherDAO pDao;
	
	@Autowired
	BorrowerDAO brwDao;
	
	@Autowired
	BookLoansDAO blDao;
	
	@Autowired
	BookCopiesDAO bcDao;
	
	@Autowired
	GenreDAO gDao;
	
	@RequestMapping(value = "/saveAuthor", method = RequestMethod.POST, consumes="application/json", produces="application/json")
	@Transactional
	public void saveAuthor(@RequestBody Author author) {
		try {
			if (author.getAuthorId() != null) {
				aDao.updateAuthor(author);
			} else {
				Integer authorId = aDao.addAuthorWithID(author);
				System.out.println("Auth Id: "+ authorId);
				//Integer authorId = aDao.addAuthorWithID(author);
				author.setAuthorId(authorId);
				aDao.addAuthorBooks(author);
			}
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/savePublisher", method = RequestMethod.POST, consumes="application/json", produces="application/json")
	@Transactional
	public void savePublisher(@RequestBody Publisher publisher) {
		try {
			if (publisher.getPublisherId() != null) {
				System.out.println("Entering");
				pDao.updatePublisher(publisher);
			} else {
				System.out.println("Entering without id");
				Integer publisherId = pDao.addPublisherWithID(publisher);
				System.out.println("PUBID : " + publisherId);
				publisher.setPublisherId(publisherId);
				for(Book b : publisher.getBooks()) {
					//b.setPublisher(publisher);
					System.out.println("Entering inside books for publisher addition");
					//bDao.addBookWithPublisherByID(b);
					bDao.addBookPublisher(b, publisherId);
					System.out.println("exiting inside books for publisher addition");
				}
			}
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/rsaveBranch", method = RequestMethod.POST, consumes="application/json", produces="application/json")
	@Transactional
	public void saveBranch(@RequestBody Branch branch)  {
		try {
			if (branch.getBranchId() != null) {
				System.out.println("Entering Branch with ID ...");
				brDao.updateBranch(branch);
			} else {
				System.out.println("Entering Branch without ID...");
				Integer branchId = brDao.addBranchWithID(branch);
				System.out.println("ID gen is : " + branchId);
				branch.setBranchId(branchId);
			}
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/saveBorrower", method = RequestMethod.POST, consumes="application/json", produces="application/json")
	@Transactional
	public void saveBorrower(@RequestBody Borrower borrower) {
		try {
			if (borrower.getCardNo() != null) {
				System.out.println("Entering with ID");
				brwDao.updateBorrowerAllFields(borrower);
			} else {
				System.out.println("Entering without id");
				Integer cardNo = brwDao.addBorrowerWithID(borrower);
				System.out.println("PUBID" + cardNo);
				borrower.setCardNo(cardNo);
			}
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/viewAuthors/{searchString}/{pageNo}", method = RequestMethod.GET, produces="application/json")
	public List<Author> readAuthors(@PathVariable String searchString, @PathVariable Integer pageNo)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		List<Author> authors = new ArrayList<>();
		if (searchString != null) {
			authors = aDao.readAuthorsByName(searchString);
		}else{
			authors = aDao.readAllAuthorsByPg(pageNo);
		}
		return authors;
	}
	
	@RequestMapping(value = "/viewAllAuthors", method = RequestMethod.GET, produces="application/json")
	public List<Author> readAuthors() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
			List<Author> authors = aDao.readAllAuthors();
			for(Author a: authors){
				a.setBooks(bDao.readBooksByAuthor(a));
			}
			return authors;
	}
	
	public Integer getAuthorsCount() {
		Integer authorCount = 0;
		try {
			authorCount = aDao.getAuthorsCount();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return authorCount;
	}
	
	@RequestMapping(value = "/viewBooks/{searchString}/{pageNo}", method = RequestMethod.GET, produces="application/json")
	public List<Book> readBooks(@PathVariable Integer pageNo, @PathVariable  String searchString) {
		List<Book> books = null;
		try {
			if(searchString!=null){
				return bDao.readBooksByName(searchString);
			}
			System.out.println("Entering read book service");
			books = bDao.readAllBooksByPg(pageNo);
			for(Book book : books) {
				book.setAuthors(aDao.readAuthorByBooks(book));
				book.setGenres(gDao.readGenresByBook(book));
				System.out.println("Publisher _____");
				//book.setPublisher(pDao.readPublisherByPK(book);
				System.out.println("Publisher ****");
			}
			
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return books;
	}
	
	public Integer getBooksCount() {
		Integer bookCount = 0;
		try {
			bookCount = bDao.getBooksCount();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return bookCount;
	}
	
	@RequestMapping(value = "/viewAllPublishers", method = RequestMethod.GET, produces="application/json")
	public List<Publisher> readPublishers() {
		List<Publisher> publishers = null;
		try {
			publishers = pDao.readAllPublisher();
			for(Publisher publisher : publishers) {
				publisher.setBooks(bDao.readBooksByPublisher(publisher));
			}
			//p.setBooks(bdao.readFirstLevel("SELECT * from tbl_book WHERE pubId = ? ",
		//	new Object[] { p.getPublisherId() }));
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return publishers;
	}
	
	@RequestMapping(value = "/viewAllBorrowers", method = RequestMethod.GET, produces="application/json")
	public List<Borrower> readAllBorrowers() {
		List<Borrower> borrowers = null;
		try {
			borrowers = brwDao.readAllBorrower();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return borrowers;
		
	}
	
	@RequestMapping(value = "/readLoans", method = RequestMethod.GET, produces="application/json")
	public List<BookLoans> readLoans() {
		List<BookLoans> bookLoans = null;
		try {
			bookLoans = blDao.readBookLoansByDateIn();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return bookLoans;
	}
	
	public Author readAuthorByPk(Integer authorId)  {
		Author author = null;
		try {
			author = aDao.readAuthorByPK(authorId);
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return author;
	}
	
	public Book readBookByPk(Integer bookId)  {
		Book book = null;
		try {
			book =  bDao.readBookByPK(bookId);
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return book;
	}
	
	public Publisher readPublisherByPk(Integer publisherId)  {
		Publisher publisher = null;
		try {
			publisher =  pDao.readPublisherByPK(publisherId);
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return publisher;
	}
	
	public Branch readBranchByPk(Integer branchId)  {
	Branch branch = null;
		try {
			branch = brDao.readBranchByPK(branchId);
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return branch;
	}
	
	public Borrower readBorrowerByPk(Integer cardNo) {
		Borrower borrower = null;
		try {
			borrower = brwDao.readBorrowerByPK(cardNo);
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return borrower;
	}
	
	@RequestMapping(value = "/viewAllBranches", method = RequestMethod.GET, produces="application/json")
	public List<Branch> readBranches()  {
		List<Branch> branches = null;
		try {
			branches = brDao.readAllBranches();
			for(Branch br : branches) {
				try {
					System.out.println();
					br.setBooks(bDao.readBooksByBranches(br));
					
				} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return branches;
	}
	
	@RequestMapping(value = "/viewAllBooks", method = RequestMethod.GET, produces="application/json")
	public List<Book> readBooks() {
		System.out.println("Entering inside read books service module");
		List<Book> books = null;
		try {
			books = bDao.readAllBooks();
			for(Book book : books) {
				book.setAuthors(aDao.readAuthorByBooks(book));
				book.setGenres(gDao.readGenresByBook(book));
				System.out.println("Publisher _____");
				//book.setPublisher(pDao.readPublisherByPK(book);
				System.out.println("Publisher ****");
			}
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return books;
	}
	
	@RequestMapping(value = "/deleteAuthor", method = RequestMethod.POST, consumes="application/json")
	public void deleteAuthor(@RequestBody Integer authorId)  {
		// delete author in Author Table
		Author author = new Author();
		author.setAuthorId(authorId);
		try {
			aDao.deleteAuthor(author);
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}//No need to delete in book author table	
	}
	
	@RequestMapping(value = "/deleteBook", method = RequestMethod.POST, consumes="application/json")
	public void deleteBook(@RequestBody Integer bookId) {
			// delete book in Book Table
		Book book = new Book();
		book.setBookId(bookId);
			try {
				bDao.deleteBook(book);
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	@RequestMapping(value = "/deletePublisher", method = RequestMethod.POST, consumes="application/json")
	public void deletePublisher(@RequestBody Integer publisherId) {
		// delete Publisher in Publisher Table
		Publisher publisher = new Publisher();
		publisher.setPublisherId(publisherId);
		try {
			pDao.deletePublisher(publisher);
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/deleteBranch", method = RequestMethod.POST, consumes="application/json")
	public void deleteBranch(@RequestBody Integer branchId) {
		// delete Branch in Branch Table
		Branch branch = new Branch();
		branch.setBranchId(branchId);
		try {
			brDao.deleteBranch(branch);
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/deleteBorrower", method = RequestMethod.POST, consumes="application/json")
	public void deleteBorrower(@RequestBody Integer cardNo) {
		Borrower borrower = new Borrower();
		borrower.setCardNo(cardNo);
		try {
			brwDao.deleteBorrower(borrower);
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/overRideDueDate", method = RequestMethod.POST, consumes="application/json")
	public void overRideDueDate(@RequestBody Integer bookId,@RequestBody  Integer branchId,@RequestBody  Integer cardNo,@RequestBody  Date duedate) {
		System.out.println("Inside admin service: " + bookId + "--" + branchId + "--" + cardNo + "--" +duedate);
		try {
			blDao.updateBookLoanDueDate(bookId, branchId, cardNo, duedate);
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/saveBook", method = RequestMethod.POST, consumes="application/json", produces="application/json")
	public void saveBook(@RequestBody Book book) {
		try {
			if (book.getBookId() != null) {
				bDao.updateBook(book);
			} else {
				Integer bookId = bDao.addBookWithPublisherByID(book);
				book.setBookId(bookId);
				bDao.addBookAuthors(book);
				bDao.addBookWithGenre(book);
			}
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	public List<Author> readAllAuthors() {
		List<Author> authors = null;
		System.out.println("Entering read authors service");
		try {
			authors = aDao.readAllAuthors();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return authors;
	}
	

	public List<Genre> readGenres() {
		List<Genre> genres = null;
		try {
			genres = gDao.readAllGenre();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return genres;
	}

}
