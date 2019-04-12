package com.capgemini.library.consumer.dao.impl;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.capgemini.library.consumer.dao.contract.BookDao;
import com.capgemini.library.consumer.dao.exceptions.DAOException;
import com.capgemini.library.consumer.dao.rowmapper.BookRM;
import com.capgemini.library.model.beans.Book;

@Named
public class BookDaoImpl implements BookDao {
	
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	//Problème avec la casse
	public List<Book> getBookByTitle(String title) throws DAOException {
		String sql = "SELECT * FROM book WHERE title LIKE ? ORDER BY title";
		String param = "%" +title+ "%" ;
		try {
			return jdbcTemplate.query(sql, new Object[] {param} , new BookRM());
		} catch (DataAccessException e) {
			throw new DAOException(e);
		} catch (Exception e) {
			throw new DAOException (e);
		}	
	}

	public List<Book> getBookByISBN(Long isbn) throws DAOException {
		String sql = "SELECT * FROM book WHERE isbn = ?";
		try {
			return jdbcTemplate.query(sql, new Object[] {isbn} , new BookRM());
		} catch (DataAccessException e) {
			throw new DAOException(e);
		} catch (Exception e) {
			throw new DAOException (e);
		}
	}

	public List<Book> getBookByAuthor(String author) throws DAOException {
		String sql = "SELECT * FROM book WHERE author LIKE ? ORDER BY title";
		String param = "%" + author + "%" ;
		try {
			return jdbcTemplate.query(sql, new Object[] {param} , new BookRM());
		} catch (DataAccessException e) {
			throw new DAOException(e);
		} catch (Exception e) {
			throw new DAOException (e);
		}
	}
	
	public Book getBookById(Integer bookId) throws DAOException{
		String sql = "SELECT * FROM book WHERE book_id = ? ";
		try {
			return jdbcTemplate.queryForObject(sql, new Object[] {bookId} , new BookRM() );
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (DataAccessException e) {
			throw new DAOException(e);
		} catch (Exception e) {
			throw new DAOException (e);
		}
	}
	
	public void updateStock(Book book) throws DAOException {
		String sql = "UPDATE book SET stock= :stock "
				+ "WHERE book_id = :bookId";
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("bookId", book.getBookId());
		namedParameters.addValue("stock", book.getStock());
		try {
			int res = namedParameterJdbcTemplate.update(sql, namedParameters);
			if (res != 1) {
				throw new DAOException ("Echec de la mise à jour du stock, aucune ligne modifiée dans la BDD");
			}
		} catch (Exception e) {
			throw new DAOException(e);
		}
	}
		
	
	/* ============  GETTERS AND SETTERS  ============= */
	public DataSource getDataSource() {
		return dataSource;
	}

	@Inject
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	



	

}
