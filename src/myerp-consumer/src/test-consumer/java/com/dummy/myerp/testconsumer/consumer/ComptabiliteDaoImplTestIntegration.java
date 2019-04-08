package com.dummy.myerp.testconsumer.consumer;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.dummy.myerp.consumer.dao.contrat.ComptabiliteDao;
import com.dummy.myerp.consumer.dao.contrat.DaoProxy;
import com.dummy.myerp.model.bean.comptabilite.CompteComptable;

@RunWith(SpringRunner.class)
@ContextConfiguration(locations = { "/**/testApplicationContext.xml" })
public class ComptabiliteDaoImplTestIntegration {
	
	@Autowired
	private DaoProxy daoProxy ;
	
	private ComptabiliteDao comptabiliteDao;

	@Before
	public void setUp() throws Exception {
		comptabiliteDao = daoProxy.getComptabiliteDao();
	}

	@Test
	public void getListCompteComptableTest() {
		List<CompteComptable> comptes = comptabiliteDao.getListCompteComptable();
		System.out.println(comptes.get(0).getLibelle());
		System.out.println(comptes.get(0).getNumero());
		assertNotNull(comptes.get(0));
	}

}
