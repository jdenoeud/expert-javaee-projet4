package com.dummy.myerp.testconsumer.consumer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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
import com.dummy.myerp.model.bean.comptabilite.LigneEcritureComptable;
import com.dummy.myerp.technical.exception.NotFoundException;

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
	public void getListEcritureComptableTest_ListSizeOK() {
		List<EcritureComptable> ecritures = comptabiliteDao.getListEcritureComptable();
		assertEquals("Nombre de champs récupérés incorrect", 5, ecritures.size());
	}
	@Test
	public void getListEcritureComptableTest_JournalCodeValueOK() {
		List<EcritureComptable> ecritures = comptabiliteDao.getListEcritureComptable();
		assertEquals("Journal récupéré incorrect", "VE", ecritures.get(1).getJournal().getCode());
	}
	@Test
	public void getEcritureComptableTest_ExistingId() throws NotFoundException {
		EcritureComptable ecriture = comptabiliteDao.getEcritureComptable(-1);
		assertNotNull("Aucun résultat", ecriture);
	}
	@Test(expected = NotFoundException.class)
	public void getEcritureComptableTest_NonExistantId() throws NotFoundException {
		EcritureComptable ecriture = comptabiliteDao.getEcritureComptable(20);
	}
	@Test
	public void getEcritureComptableTest_ExistingId_NbLignesComptableOK() throws NotFoundException {
		EcritureComptable ecriture = comptabiliteDao.getEcritureComptable(-1);
		List<LigneEcritureComptable> lignes = ecriture.getListLigneEcriture();
		assertEquals("Lignes écritures comptables non récupérées", "TVA 20%",lignes.get(1).getLibelle());
	}
	@Test
	public void getEcritureComptableByRefTest_ExistingRef() throws NotFoundException {
		EcritureComptable ecriture = comptabiliteDao.getEcritureComptableByRef("VE-2016/00002");
		assertEquals("TMA Appli Xxx", ecriture.getLibelle());
	}
	@Test(expected = NotFoundException.class)
	public void getEcritureComptableByRefTest_NonExistantRef() throws NotFoundException {
		EcritureComptable ecriture = comptabiliteDao.getEcritureComptableByRef("VE-201/00005");
	}
	@Test
	public void loadListLigneEcritureTest_GivenNonExistantEcritureComptableId()  {
        EcritureComptable ecritureTest = new EcritureComptable();
        ecritureTest.setId(10);
        ecritureTest.setJournal(new JournalComptable("BQ","Banque"));
        ecritureTest.setLibelle("Paiement Facture C110002");
        comptabiliteDao.loadListLigneEcriture(ecritureTest);
        assertEquals(0,ecritureTest.getListLigneEcriture().size());
	}
	@Test
	public void loadListLigneEcritureTest_GivenExistingEcritureComptId_NbLigneEcritureOK() {
        EcritureComptable ecritureTest = new EcritureComptable();
        ecritureTest.setId(-2);
        ecritureTest.setJournal(new JournalComptable("AC","Achat"));
        ecritureTest.setLibelle("TMA Appli Xxx");
        comptabiliteDao.loadListLigneEcriture(ecritureTest);
        assertEquals(3,ecritureTest.getListLigneEcriture().size());
	}
	
	 // ==================== EcritureComptable - INSERT ====================
	@Test
	public void insertEcritureComptableTest_GivenNewEcritureComptable()  {
		Integer beforeNb = comptabiliteDao.getListEcritureComptable().size()  ;
        EcritureComptable ecritureTest = new EcritureComptable();
        ecritureTest.setJournal(new JournalComptable("AC","Achat"));
        ecritureTest.setReference("AC-2016/00006");
        ecritureTest.setDate(new Date());
        ecritureTest.setLibelle("Bureaux");
        comptabiliteDao.insertEcritureComptable(ecritureTest);
        Integer expectedSize = beforeNb + 1 ;
        Integer actualSize = comptabiliteDao.getListEcritureComptable().size() ;
        assertEquals(expectedSize, actualSize);
	}
	
	@Test
	public void insertListLigneEcritureComptableTest() throws NotFoundException {
		Integer beforeNb = comptabiliteDao.getListEcritureComptable().size()  ;
		LigneEcritureComptable ligne1 = new LigneEcritureComptable(new CompteComptable(411,"Clients"), null, new BigDecimal(18), null);
		LigneEcritureComptable ligne2 = new LigneEcritureComptable(new CompteComptable(512,"Banque"), null, null, new BigDecimal(18));
		List<LigneEcritureComptable> lignes = new ArrayList<>() ;
		lignes.add(ligne1);
		lignes.add(ligne2);
		EcritureComptable ecritureTest = new EcritureComptable() ;
        ecritureTest.setJournal(new JournalComptable("AC","Achat"));
        ecritureTest.setReference("AC-2016/00007");
        ecritureTest.setDate(new Date());
        ecritureTest.setLibelle("Bureaux");
		ecritureTest.getListLigneEcriture().addAll(lignes);
		comptabiliteDao.insertEcritureComptable(ecritureTest);
		EcritureComptable ecritureResultat = comptabiliteDao.getEcritureComptableByRef("AC-2016/00007");
		assertEquals(2,ecritureResultat.getListLigneEcriture().size());
	}

	

	
	


}
