package com.capgemini.library.consumer.dao.contract;
import java.util.List;

import javax.inject.Named;

import com.capgemini.library.consumer.dao.exceptions.DAOException;
import com.capgemini.library.model.beans.Book;

@Named
public interface BookDao {
	
	public List<Book> getBookByTitle(String title) throws DAOException ;
	 
	public List<Book> getBookByISBN(Long isbn) throws DAOException;
	
	public List<Book> getBookByAuthor(String title) throws DAOException;
	
	public Book getBookById(Integer bookId) throws DAOException;
	
	public void updateStock(Book book) throws DAOException;
}
