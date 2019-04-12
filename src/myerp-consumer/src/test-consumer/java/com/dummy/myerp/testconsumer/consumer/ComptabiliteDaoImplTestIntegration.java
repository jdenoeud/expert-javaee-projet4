package com.dummy.myerp.testconsumer.consumer;

import static org.junit.Assert.assertEquals;

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
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;

@RunWith(SpringRunner.class)
@ContextConfiguration(locations = { "/**/testApplicationContext.xml" })
public class ComptabiliteDaoImplTestIntegration {
	
	@Autowired
	private DaoProxy daoProxy;
	
	private ComptabiliteDao comptabiliteDao;

	@Before
	public void setUp() throws Exception {
		comptabiliteDao = daoProxy.getComptabiliteDao();
	}
	
	// ==================== Méthode GetListCompteComptable ====================
	@Test
	public void getListCompteComptableTest_ListSizeOk() {
		List<CompteComptable> comptes = comptabiliteDao.getListCompteComptable();
		assertEquals("Longueur de la liste incorrecte",7, comptes.size());
	}
	
	@Test
	public void getListCompteComptableTest_VerifyFirstElement() {
		List<CompteComptable> comptes = comptabiliteDao.getListCompteComptable();
		assertEquals("Valeur du libellé incorrect", "Fournisseurs",comptes.get(0).getLibelle());
	}
	
	// ==================== Méthode GetListJournalComptable ====================
	@Test
	public void getListJournalComptableTest_ListSizeOk() {
		List<JournalComptable> journaux = comptabiliteDao.getListJournalComptable();
		assertEquals("Nombre de champs récupérés incorrect", 4, journaux.size());
	}
	
	@Test
	public void getListJournalComptableTest_LastCodeValueOk() {
		List<JournalComptable> journaux = comptabiliteDao.getListJournalComptable();
		assertEquals("Valeur du code incorrecte", "OD", journaux.get(3).getCode());
	}
	@Test
	public void getListJournalComptableTest_FirstLibelleValueOk() {
		List<JournalComptable> journaux = comptabiliteDao.getListJournalComptable();
		assertEquals("Valeur du libelle incorrecte", "Achat", journaux.get(0).getLibelle());
	}
	
    // ==================== EcritureComptable - GET ====================
	@Test
	public void getListEcritureComptableTest_ListSizeOk() {
		List<EcritureComptable> ecritures = comptabiliteDao.getListEcritureComptable();
		assertEquals("Nombre de champs récupérés incorrect", 5, ecritures.size());
	}


}
