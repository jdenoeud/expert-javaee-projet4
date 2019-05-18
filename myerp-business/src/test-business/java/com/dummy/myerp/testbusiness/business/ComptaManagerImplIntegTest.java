package com.dummy.myerp.testbusiness.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.dummy.myerp.business.impl.manager.ComptabiliteManagerImpl;
import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.model.bean.comptabilite.LigneEcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.SoldeCompteComptable;
import com.dummy.myerp.technical.exception.FunctionalException;

public class ComptaManagerImplIntegTest extends BusinessTestCase {

    private ComptabiliteManagerImpl manager = new ComptabiliteManagerImpl();
    
    @Rule 
	public ExpectedException expectedEx = ExpectedException.none();
	
    private EcritureComptable vEcritureComptable;
    
    @Before
    public void initializeEcritureComptable() {
    	vEcritureComptable = new EcritureComptable();
    	vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
	    vEcritureComptable.setLibelle("Libelle");
    	vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(401),
               null, new BigDecimal(123),
               null));
    	vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(606),
               null, null,
               new BigDecimal(123)));
    }
    
// ==================== Test de la méthode getSoldeCompteComptable ====================
    
    // RG_Compta_1 : teste que la solde calculé soit exact et que le solde soit bien débiteur 
    // dans le cas où plusieurs lignes d'écriture existent pour ce compte
    
    @Test
    public void getSoldeCompteComptableTest_whenCompteWithLigneEcriture_returnSucess() {
    	CompteComptable compte = new CompteComptable(512, "Banque");
    	
    	SoldeCompteComptable compteActual = manager.getSoldeCompteComptable(compte.getNumero());
    	assertTrue(new BigDecimal("2947.26").compareTo(compteActual.getValeur())==0);
    	assertEquals("Solde débiteur", compteActual.getLibelle());
    }
    
    // RG_Compta_1 : teste que la solde calculé soit exact et que le solde soit bien créditeur 
    @Test
    public void getSoldeCompteComptableTest_whenCompteWithLigneEcriture_returnCompteCrediteur() {
    	CompteComptable compte = new CompteComptable(706, "Prestations de services");
    	
    	SoldeCompteComptable compteActual = manager.getSoldeCompteComptable(compte.getNumero());
    	assertTrue(new BigDecimal("-7250").compareTo(compteActual.getValeur())==0);
    	assertEquals("Solde créditeur", compteActual.getLibelle());
    	
    }
    
    // RG_Compta_1 : teste que la solde est nul dans le cas où aucune ligne associée au compteComptable
    @Test
    public void getSoldeCompteComptableTest_whenCompteWithNoLigneEcriture_returnSoldeNul() {
    	CompteComptable compte = new CompteComptable(805, "Compte fictif");
    	
    	SoldeCompteComptable compteActual = manager.getSoldeCompteComptable(compte.getNumero());
    	assertEquals(BigDecimal.ZERO, compteActual.getValeur());
    	assertEquals("Solde nul",compteActual.getLibelle());
    }
    
// ==================== Test de la méthode AddReference ====================
    
	//RG_Compta_5 : teste l'ajout de la référence quand c'est le 1er enregistrement de l'annee concernée
	@Test
	public synchronized void addReferenceTest_whenNoSequenceExistsYet() {
		vEcritureComptable.setDate(new Date());
	    manager.addReference(vEcritureComptable);
	    
	    assertEquals("AC-2019/00001", vEcritureComptable.getReference());
	}
    
    //RG_Compta_5 : teste l'ajout de la référence quand il existe déjà des enregistrements pour l'année concernée
    @Test
    public synchronized void addReferenceTest_whenLastSequenceExists() {
    	Calendar calendar = new GregorianCalendar(2016, Calendar.DECEMBER, 31);
        vEcritureComptable.setDate(calendar.getTime());
        manager.addReference(vEcritureComptable);
        
        assertEquals("AC-2016/00041", vEcritureComptable.getReference());
    }
    
  // ==================== Test de la méthode checkEcritureComptableContext ====================
    
    //RG_Compta_6 : teste l'unicité de la référence comptable avec une écriture comptable qui existe déjà
    @Test
    public void checkEcritureComptableContext_whenReferenceAlreadyExists() throws Exception {  
    	expectedEx.expect(FunctionalException.class);
    	expectedEx.expectMessage("Une autre écriture comptable existe déjà avec la même référence.");
	  	Calendar calendar = new GregorianCalendar(2016, Calendar.DECEMBER, 31);
    	vEcritureComptable.setDate(calendar.getTime());
    	vEcritureComptable.setReference("AC-2016/00001");

    	manager.checkEcritureComptable(vEcritureComptable);
    }
    
    //RG_Compta_6 : teste l'unicité de la référence comptable avec une écriture comptable qui existe déjà  mais avec un id différent
    @Test
    public void checkEcritureComptableContext_whenReferenceExists_but_Id_is_different() throws Exception {  
    	expectedEx.expect(FunctionalException.class);
    	expectedEx.expectMessage("Une autre écriture comptable existe déjà avec la même référence.");
    	vEcritureComptable.setId(10);
	  	Calendar calendar = new GregorianCalendar(2016, Calendar.DECEMBER, 31);
    	vEcritureComptable.setDate(calendar.getTime());
    	vEcritureComptable.setReference("AC-2016/00001");

    	manager.checkEcritureComptable(vEcritureComptable);
    }  
   
    //RG_Compta_6 : teste l'unicité de la référence comptable avec une écriture comptable qui n'existe pas encore   
    @Test
    public void checkEcritureComptable_whenReferenceDoesNotExist() throws Exception {  
    	vEcritureComptable.setDate(new Date());
    	vEcritureComptable.setReference("AC-2019/00245");
    	try {
    		manager.checkEcritureComptable(vEcritureComptable);
    	} catch (Exception e) {
    		fail("Exception levée : "+ e.getMessage());
    	}   	
    }
    
 // ==================== Test de la méthode InsertEcritureComptable ====================
    // Teste l'ajout avec succès d'une écriture comptable respectant toutes les règles de gestion
    @Test
    public void insertEcritureComptableTest_givenCorrectEcritureComptable_returnSuccess() throws Exception {
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Fauteuil de bureau");
        vEcritureComptable.setReference("AC-2019/00250");
        manager.insertEcritureComptable(vEcritureComptable);
        List<EcritureComptable> ecritures = manager.getListEcritureComptable();
        EcritureComptable ecriture = EcritureComptable.getInListByReference(ecritures, "AC-2019/00250");
        
        assertEquals("Fauteuil de bureau", ecriture.getLibelle());
    }
    
    // ==================== Test de la méthode UpdateEcritureComptable ====================
    //Teste la modification du libellé et des lignes d'écritrures pour l'écriture comptable d'Id -3
    @Test
    public void updateEcritureComptableTest_givenCorrectEcritureComptable_returnSuccess() throws Exception {
    	List<EcritureComptable> initialEcritures = manager.getListEcritureComptable();
        EcritureComptable initialEcriture = EcritureComptable.getInListByReference(initialEcritures, "BQ-2016/00003");
        initialEcriture.setLibelle("Ecran 24 pouces");
        initialEcriture.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(401),
                                                                                 null, new BigDecimal(123),
                                                                                 null));
        initialEcriture.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(606),
                                                                                 null, null,
                                                                                 new BigDecimal(123)));
        manager.updateEcritureComptable(initialEcriture);        
        List<EcritureComptable> modifiedEcritures = manager.getListEcritureComptable();
        EcritureComptable modifiedEcriture = EcritureComptable.getInListByReference(modifiedEcritures, "BQ-2016/00003");
        
        assertEquals("Le libellé n'a pas été mis à jour correctement","Ecran 24 pouces", modifiedEcriture.getLibelle());
        assertEquals("Les lignes d'écritures n'ont pas été mises à jour", 4, modifiedEcriture.getListLigneEcriture().size());
    }
    
    // ==================== Test de la méthode DeleteEcritureComptable ====================
    //Teste la suppression de l'EcritureComptable d'id -5
    @Test
    public void deleteEcritureComptableTest_givenExistingEcritureComptable_returnSuccess() throws Exception {
        manager.deleteEcritureComptable(-5);
        List<EcritureComptable> modifiedEcritures = manager.getListEcritureComptable();
        
        assertNull("Le libellé n'a pas été mis à jour correctement", EcritureComptable.getInListByReference(modifiedEcritures, "BQ-2016/00005"));
    }
    
}
