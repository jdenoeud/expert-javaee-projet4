package com.capgemini.library.consumer.dao.contract;


import java.util.List;

import javax.inject.Named;

import com.capgemini.library.consumer.dao.exceptions.DAOException;
import com.capgemini.library.model.beans.Borrowing;

@Named
public interface BorrowingDao {

	public List<Borrowing> getBorrowingByCustomerId(Integer customerId) throws DAOException ;

	public List<Borrowing> getOutdatedBorrowings() throws DAOException ;
	
	public Borrowing getBorrowingById(Integer id) throws DAOException ;
	
	public void addBorrowing(Borrowing borrowing) throws DAOException ;
	
	public void extendBorrowing(Borrowing borrowing) throws DAOException;
	
	public void updateBorrowing(Borrowing borrowing) throws DAOException;

}
