package com.dummy.myerp.testconsumer.consumer;

import static org.junit.Assert.assertEquals;
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
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;

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
	public void getListCompteComptableTest_ListNotNull() {
		List<CompteComptable> comptes = comptabiliteDao.getListCompteComptable();
		assertNotNull("Aucune liste retournée", comptes.get(0));
	}
	
	@Test
	public void getListCompteComptableTest_ListSizeOk() {
		List<CompteComptable> comptes = comptabiliteDao.getListCompteComptable();
		assertEquals("Longueur de la liste incorrecte",7, comptes.size());
	}
	
	@Test
	public void getListCompteComptableTest_VerifyFirstValue() {
		List<CompteComptable> comptes = comptabiliteDao.getListCompteComptable();
		assertEquals("Valeur du libellé incorrect", "Fournisseurs",comptes.get(0).getLibelle());
	}
	
	@Test
	public void getListJournalComptableTest_LastLibelleValueOk() {
		List<JournalComptable> journaux = comptabiliteDao.getListJournalComptable();
		assertEquals("Valeur du libellé incorrecte", "Opérations diverses", journaux.get(3).getLibelle());
	}
	
	@Test
	public void getListJournalComptableTest_LastCodeValueOk() {
		List<JournalComptable> journaux = comptabiliteDao.getListJournalComptable();
		assertEquals("Valeur du code journal incorrecte", "OD", journaux.get(3).getCode());
	}

}
