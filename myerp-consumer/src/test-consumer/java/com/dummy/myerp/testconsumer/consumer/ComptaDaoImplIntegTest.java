package com.dummy.myerp.testconsumer.consumer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.dummy.myerp.consumer.dao.contrat.ComptabiliteDao;
import com.dummy.myerp.consumer.dao.contrat.DaoProxy;
import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.model.bean.comptabilite.LigneEcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.SequenceEcritureComptable;
import com.dummy.myerp.technical.exception.NotFoundException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/com/dummy/myerp/testconsumer/consumer/testApplicationContext.xml" })
public class ComptaDaoImplIntegTest {
	
	@Autowired
	@Qualifier(value="DaoProxy")
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
	
    // ==================== JournalComptable - GET ====================
	@Test
	public void getJournalComptableByCode_WhenCodeIsBQ_returnBanque() throws NotFoundException {
		JournalComptable journal = comptabiliteDao.getJournalComptableByCode("BQ");
		assertEquals("Banque", journal.getLibelle());
	}
	@Test(expected = NotFoundException.class)
	public void getJournalComptableByCode_WhenCodeIsFalse_returnNotFoundException() throws NotFoundException {
		JournalComptable journal = comptabiliteDao.getJournalComptableByCode("TOTO");
	}
	
    // ==================== SequenceEcritureComptable - GET ====================
	@Test
	public void getLastSequenceByCodeAndYear_journalODandYear2016_return88() throws NotFoundException {
		SequenceEcritureComptable lastSequence = comptabiliteDao.getLastSequenceByCodeAndYear("OD", 2016);
		assertEquals(Integer.valueOf(88), lastSequence.getDerniereValeur());
	}
	@Test(expected = NotFoundException.class)
	public void getLastSequenceByCodeAndYear_returnNotFoundException() throws NotFoundException {
		SequenceEcritureComptable lastSequence = comptabiliteDao.getLastSequenceByCodeAndYear("OD", 2021);
	}
    // ==================== EcritureComptable - GET ====================
	@Test
	public void getListEcritureComptableTest_ListiSNotNull() {
		List<EcritureComptable> ecritures = comptabiliteDao.getListEcritureComptable();
		assertFalse( ecritures.isEmpty());
	}
	@Test
	public void getListEcritureComptableTest_JournalCodeValueOK() {
		List<EcritureComptable> ecritures = comptabiliteDao.getListEcritureComptable();
		assertEquals("Journal récupéré incorrect", "AC", ecritures.get(0).getJournal().getCode());
	}
	@Test
	public void getEcritureComptableTest_ExistingId() throws NotFoundException {
		EcritureComptable ecriture = comptabiliteDao.getEcritureComptable(-2);
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
	public void insertEcritureComptableTest_GivenNewEcritureComptable() throws NotFoundException  {
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
        //Fin du test, suppression de la l'écriture commptable créée
        EcritureComptable ecritureASupprimer = comptabiliteDao.getEcritureComptableByRef("AC-2016/00006");
        comptabiliteDao.deleteEcritureComptable(ecritureASupprimer.getId());
        
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
		assertEquals("Nombre de lignes comptables insérées incorrect",2,ecritureResultat.getListLigneEcriture().size());
        //Fin du test, suppression de la l'écriture commptable créée
        EcritureComptable ecritureASupprimer = comptabiliteDao.getEcritureComptableByRef("AC-2016/00007");
        comptabiliteDao.deleteEcritureComptable(ecritureASupprimer.getId());
	}
	
    // ==================== EcritureComptable - UPDATE ====================
	@Test
	public void updateEcritureComptableTest() throws NotFoundException  {
		EcritureComptable ecritureTest = comptabiliteDao.getEcritureComptable(-4);
        ecritureTest.setJournal(new JournalComptable("AC","Achat"));
        ecritureTest.setReference("AC-2017/00010");
        Calendar c1 = Calendar.getInstance();
        c1.set(2019,03,14,0,0,0);
        ecritureTest.setDate(c1.getTime());;
        ecritureTest.setLibelle("Papiers");
        comptabiliteDao.updateEcritureComptable(ecritureTest);
        EcritureComptable ecritureActual = comptabiliteDao.getEcritureComptable(-4);
        assertEquals("Le code du journal n'a pas été mis à jour correctement,","AC", ecritureActual.getJournal().getCode());
        assertEquals("La référence n'a pas été mise à jour correctement", "AC-2017/00010", ecritureActual.getReference());
        assertEquals("La date n'es pas été mise à joure correctement","2019-04-14",ecritureActual.getDate().toString());
        assertEquals("Le libellé n'a pas été mis à jour correctement","Papiers", ecritureActual.getLibelle());
        //Fin du test - re-initialisation des données :
        ecritureTest.setJournal(new JournalComptable("VE","Vente"));
        ecritureTest.setReference("VE-2016/00004");
        Calendar c2 = Calendar.getInstance();
        c2.set(2016,12,28,0,0,0);
        ecritureTest.setDate(c2.getTime());;
        ecritureTest.setLibelle("TMA Appli Yyy");
        comptabiliteDao.updateEcritureComptable(ecritureTest);      
	}
	
    // ==================== EcritureComptable - DELETE ====================
//	@Test
//	public void deleteEcritureComptableTest_ExistingId() throws NotFoundException  {
//		Integer initialSize = comptabiliteDao.getListEcritureComptable().size()  ;
//		EcritureComptable ecritureBackUp = comptabiliteDao.getEcritureComptable(-3);
//		comptabiliteDao.deleteEcritureComptable(-3);
//		Integer expectedSize = initialSize - 1;
//		Integer actualSize = comptabiliteDao.getListEcritureComptable().size();
//		assertEquals(expectedSize,actualSize);
//		//Fin du test, je remets l'écriture supprimée en base de données
//		comptabiliteDao.insertEcritureComptable(ecritureBackUp);
//	}

}
