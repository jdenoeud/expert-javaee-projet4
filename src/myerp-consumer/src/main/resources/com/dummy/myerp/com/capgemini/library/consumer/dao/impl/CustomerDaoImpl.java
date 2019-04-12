package com.capgemini.library.consumer.dao.impl;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.capgemini.library.consumer.dao.contract.CustomerDao;
import com.capgemini.library.consumer.dao.exceptions.DAOException;
import com.capgemini.library.consumer.dao.rowmapper.CustomerRM;
import com.capgemini.library.model.beans.Customer;

@Named
public class CustomerDaoImpl implements CustomerDao {
	
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	public void addCustomer(Customer customer) throws DAOException {
		String sql = "INSERT INTO customer (name, firstname, email, password) "
				+ "VALUES (:name, :firstname, :email, :password)";
		/*SqlParameterSource namedParameters = new MapSqlParameterSource(
				"pseudo", customer.getPseudo())
				.addValue("name", customer.getName())
				.addValue("firstname", customer.getFirstname())
				.addValue("email", customer.getEmail())
				.addValue("password", customer.getPassword()); */
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("name", customer.getName());
		namedParameters.addValue("firstname", customer.getFirstname());
		namedParameters.addValue("email", customer.getEmail());
		namedParameters.addValue("password", customer.getPassword());
		try {
			int res = namedParameterJdbcTemplate.update(sql, namedParameters);
			if (res != 1) 
				throw new DAOException ("Echec de l'ajout de l'utilisateur, aucune ligne modifiée dans la BDD");
		} catch (DataAccessException e) {
			throw new DAOException("erreur " + e );
		} catch (Exception e) {
			throw new DAOException (e);
		}
	}


	public Customer getCustomerByPseudo(String pseudo) throws DAOException{
		 String sql = "SELECT COUNT(*) FROM customer WHERE pseudo = ? ";
		 try {
			 return jdbcTemplate.queryForObject(sql, new Object[] {pseudo} , new CustomerRM());
		} catch (DataAccessException e) {
			throw new DAOException(e);
		} catch (Exception e) {
			throw new DAOException (e);
		}
	}

	public Customer getCustomerByEmail(String email) throws DAOException {
		String sql = "SELECT * FROM customer WHERE email = ? ";
		try {
			return jdbcTemplate.queryForObject(sql, new Object[] {email} , new CustomerRM());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}  catch (DataAccessException e) {
			throw new DAOException(e);
		} catch(Exception e) {
			throw new DAOException (e);
		}
	}
	
	public void deleteCustomer(Integer customerId) throws DAOException {
		String sql_delete = "DELETE FROM customer WHERE customer_id = ?";
		try {
			int res = jdbcTemplate.update(sql_delete, new Object[] {customerId});
		    if (res != 1) 
		    	throw new DAOException ("Echec de l'ajout de l'utilisateur, aucune ligne modifiée dans la BDD");
		} catch (DataAccessException e) {
			throw new DAOException(e);
		} catch (Exception e) {
			throw new DAOException (e);
		}
		
	}
	
	public Customer getCustomerById(Integer customerId) throws DAOException {
		String sql = "SELECT * FROM customer WHERE customer_id = ? ";
		try {
			return jdbcTemplate.queryForObject(sql, new Object[] {customerId} , new CustomerRM());
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (DataAccessException e) {
			throw new DAOException(e);
		} catch (Exception e) {
			throw new DAOException (e);
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
