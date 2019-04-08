package com.dummy.myerp.testbusiness.business;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.dummy.myerp.business.impl.manager.ComptabiliteManagerImpl;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;

public class TestIntegration extends BusinessTestCase{

	private ComptabiliteManagerImpl manager = new ComptabiliteManagerImpl();

	 @Before
	 public void setUp() {
		 	
	 }
	
	@Test
	public void test() {
		List <JournalComptable> listJournalComptable = manager.getListJournalComptable();
		System.out.println(listJournalComptable.get(0).getCode());
		assertNotNull(listJournalComptable);
	}

}
