package com.capgemini.library.consumer.dao.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.capgemini.library.model.beans.Book;

public class BookRM implements RowMapper<Book> {
	
	public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
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
		return book ;
	}
}
