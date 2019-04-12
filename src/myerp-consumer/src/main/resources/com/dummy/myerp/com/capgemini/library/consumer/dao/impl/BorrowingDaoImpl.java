package com.capgemini.library.consumer.dao.impl;

import java.sql.Timestamp;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.capgemini.library.consumer.dao.contract.BorrowingDao;
import com.capgemini.library.consumer.dao.exceptions.DAOException;
import com.capgemini.library.consumer.dao.rowmapper.BorrowingRM;
import com.capgemini.library.model.beans.Borrowing;


// isExtendable peut prendre 2 valeurs:
// 	0 = false
// 	1 = true

@Named
public class BorrowingDaoImpl implements BorrowingDao{
	
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	public List<Borrowing> getBorrowingByCustomerId(Integer customerId) throws DAOException {
		String sql = "SELECT br.id, br.customer_id_fk, br.start_date, br.end_date, br.is_extendable, br.status, "
				+ "bk.book_id, bk.isbn, bk.title, bk.author, bk.abstract, bk.category, bk.cover_page, bk.publisher, bk.stock, "
				+ "c.customer_id, c.name, c.firstname, c.email, c.password "
				+ "FROM borrowing AS br "
				+ "INNER JOIN book AS bk "
				+ "ON br.book_id_fk = bk.book_id "
				+ "INNER JOIN customer AS c "
				+ "ON br.customer_id_fk = c.customer_id "
				+ "WHERE br.customer_id_fk = ? "
				+ "ORDER BY start_date DESC";
		try {
			return jdbcTemplate.query(sql, new Object[] {customerId} , new BorrowingRM());
		} catch (DataAccessException e) {
			throw new DAOException(e);
		} catch(Exception e) {
			throw new DAOException (e);
		}
	}
	
	@Override
	public List<Borrowing> getOutdatedBorrowings() throws DAOException {
		String sql = "SELECT br.id, br.customer_id_fk, br.start_date, br.end_date, br.is_extendable, br.status, "
				+ "bk.book_id, bk.isbn, bk.title, bk.author, bk.abstract, bk.category, bk.cover_page, bk.publisher, bk.stock, "
				+ "c.customer_id, c.name, c.firstname, c.email, c.password "
				+ "FROM borrowing AS br "
				+ "INNER JOIN book AS bk "
				+ "ON br.book_id_fk = bk.book_id "
				+ "INNER JOIN customer AS c "
				+ "ON br.customer_id_fk = c.customer_id "
				+ "WHERE br.status = 'ENCOURS' AND DATEDIFF(NOW(), br.end_date )>0 "
				+ "ORDER BY start_date DESC";
		try {
			return jdbcTemplate.query(sql , new BorrowingRM());
		} catch (DataAccessException e) {
			throw new DAOException(e);
		} catch(Exception e) {
			throw new DAOException (e);
		}
	}
	
	public Borrowing getBorrowingById(Integer id) throws DAOException {
		String sql = "SELECT br.id, br.customer_id_fk, br.start_date, br.end_date, br.is_extendable, br.status, "
				+ "bk.book_id, bk.isbn, bk.title, bk.author, bk.abstract, bk.category, bk.cover_page, bk.publisher, bk.stock, "
				+ "c.customer_id, c.name, c.firstname, c.email, c.password "
				+ "FROM borrowing AS br "
				+ "INNER JOIN book AS bk "
				+ "ON br.book_id_fk = bk.book_id "
				+ "INNER JOIN customer AS c "
				+ "ON br.customer_id_fk = c.customer_id "
				+ "WHERE br.id = ? "
				+ "ORDER BY start_date DESC";
		try {
			return jdbcTemplate.queryForObject(sql, new Object[] {id} , new BorrowingRM());
		} catch( EmptyResultDataAccessException e) {
			return null;
		}
		catch (DataAccessException e) {
			throw new DAOException(e);
		} catch(Exception e) {
			throw new DAOException (e);
		}
	}

	public void addBorrowing(Borrowing borrowing) throws DAOException {
		String sql = "INSERT INTO borrowing (customer_id_fk, book_id_fk, start_date, end_date, is_extendable, status) "
				+ "VALUES (:customerIdFk, :bookIdFk, :startDate, :endDate, :isExtendable, :status)";
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("customerIdFk", borrowing.getCustomer().getId());
		namedParameters.addValue( "bookIdFk", borrowing.getBook().getBookId() );
		namedParameters.addValue("startDate", new Timestamp(borrowing.getStartDate().getMillis()).toString() );
		namedParameters.addValue("endDate", new Timestamp(borrowing.getEndDate().getMillis()).toString() );
		if (borrowing.getIsExtendable()) {
			namedParameters.addValue("isExtendable", 1); //1 = true
		} else {
			namedParameters.addValue("isExtendable", 0); //0 = false
		}
		namedParameters.addValue("status", borrowing.getStatus() );

		try {
			int res = namedParameterJdbcTemplate.update(sql, namedParameters);
			if (res != 1) {
				throw new DAOException ("Echec de l'ajout du prêt, aucune ligne modifiée dans la BDD");
			}
		} catch (DataAccessException e) {
			throw new DAOException(e);
		} catch(Exception e) {
			throw new DAOException (e);
		}
	}
	
	public void extendBorrowing (Borrowing borrowing) throws DAOException {
			String sql = "UPDATE borrowing SET start_date= :startDate, end_date= :endDate, is_extendable = :isExtendable "
					+ "WHERE customer_id_fk = :customerIdFk AND book_id_fk = :bookIdFk ";
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("customerIdFk", borrowing.getCustomer().getId());
			namedParameters.addValue("bookIdFk", borrowing.getBook().getBookId());
			namedParameters.addValue("startDate", new Timestamp(borrowing.getStartDate().getMillis()).toString());
			namedParameters.addValue("endDate", new Timestamp(borrowing.getEndDate().getMillis()).toString());
			if (borrowing.getIsExtendable()) {
				namedParameters.addValue("isExtendable", 1); //1 = true
			} else {
				namedParameters.addValue("isExtendable", 0); //0 = false
			}
			try {
				int res = namedParameterJdbcTemplate.update(sql, namedParameters);
				if (res != 1) {
					throw new DAOException ("Echec de la prolongation du prêt, aucune ligne modifiée dans la BDD");
				}
			} catch (DataAccessException e ) {
				throw new DAOException(e);
			} catch (Exception e) {
				throw new DAOException(e);
			}
	}
	
	@Override
	public void updateBorrowing(Borrowing borrowing) throws DAOException {
		String sql = "UPDATE borrowing SET start_date= :startDate, end_date= :endDate, is_extendable = :isExtendable, status= :status "
				+ "WHERE id = :borrowingId ";
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("borrowingId", borrowing.getId());
		namedParameters.addValue("startDate", new Timestamp(borrowing.getStartDate().getMillis()).toString() );
		namedParameters.addValue("endDate", new Timestamp(borrowing.getEndDate().getMillis()).toString());
		if (borrowing.getIsExtendable()) {
			namedParameters.addValue("isExtendable", 1); //1 = true
		} else {
			namedParameters.addValue("isExtendable", 0); //0 = false
		}
		namedParameters.addValue("status", borrowing.getStatus());
		try {
			int res = namedParameterJdbcTemplate.update(sql, namedParameters);
			if (res != 1) {
				throw new DAOException ("Echec de la mise à jour du prêt en base de données");
			}
		} catch (DataAccessException e ) {
			throw new DAOException(e);
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
