package com.capgemini.library.consumer.dao.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.joda.time.DateTime;
import org.springframework.jdbc.core.RowMapper;

import com.capgemini.library.model.beans.Book;
import com.capgemini.library.model.beans.Borrowing;
import com.capgemini.library.model.beans.Customer;

public class BorrowingRM implements RowMapper<Borrowing>{
	
	public Borrowing mapRow(ResultSet rs, int rowNum) throws SQLException {
		Borrowing borrowing = new Borrowing();
		borrowing.setId(rs.getInt("id"));
		borrowing.setStartDate( new DateTime(rs.getTimestamp("start_date")));
		borrowing.setEndDate(new DateTime( rs.getTimestamp("end_date")));
		borrowing.setIsExtendable(rs.getBoolean("is_extendable"));
		borrowing.setStatus(rs.getString("status"));
		Book book = new Book();
		book.setBookId(rs.getInt("book_id"));
		book.setIsbn(rs.getLong("isbn"));
		book.setTitle(rs.getString("title"));
		book.setAuthor(rs.getString("author"));
		book.setBookAbstract(rs.getString("abstract"));
		book.setCategory(rs.getString("category"));
		book.setCoverPage(rs.getString("cover_page"));
		book.setPublisher(rs.getString("publisher"));
		book.setStock(rs.getInt("stock"));
		borrowing.setBook(book);
		Customer customer = new Customer();
		customer.setId(rs.getInt("customer_id"));
		customer.setName(rs.getString("name"));
		customer.setEmail(rs.getString("email"));
		customer.setPassword(rs.getString("password"));
		customer.setFirstname(rs.getString("firstname"));
		borrowing.setCustomer(customer);
		return borrowing ;
	}
}
