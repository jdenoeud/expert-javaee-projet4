package com.dummy.myerp.consumer.dao.impl.db.dao;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.dummy.myerp.model.bean.comptabilite.CompteComptable;

public class ComptabiliteDaoImplTest {
	private ComptabiliteDaoImpl pComptabiliteDaoImpl ;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ComptabiliteDaoImpl pComptabiliteDaoImpl = ComptabiliteDaoImpl.getInstance();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void getListCompteComptableTest() {
		List<CompteComptable> listeComptes = pComptabiliteDaoImpl.getListCompteComptable();
	}

}
