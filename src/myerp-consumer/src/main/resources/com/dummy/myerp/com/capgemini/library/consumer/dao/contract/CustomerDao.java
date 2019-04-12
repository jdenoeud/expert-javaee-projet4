package com.capgemini.library.consumer.dao.contract;

import javax.inject.Named;

import com.capgemini.library.consumer.dao.exceptions.DAOException;
import com.capgemini.library.model.beans.Customer;

@Named
public interface CustomerDao {
	
	public void addCustomer(Customer customer) throws DAOException;
	
	public Customer getCustomerById(Integer customerId) throws DAOException;
	
	public Customer getCustomerByPseudo(String pseudo) throws DAOException;
	
	public Customer getCustomerByEmail(String email) throws DAOException;
	
	public void deleteCustomer(Integer customerId) throws DAOException;
}
