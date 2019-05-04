package com.dummy.myerp.testbusiness.business;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.dummy.myerp.business.impl.manager.ComptabiliteManagerImpl;
import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.model.bean.comptabilite.LigneEcritureComptable;

public class ComptabiliteManagerTI extends BusinessTestCase {
	
    private ComptabiliteManagerImpl manager = new ComptabiliteManagerImpl();
    
    @Rule 
	public ExpectedException expectedEx = ExpectedException.none();
	  
	//RG_Compta_5 : teste l'ajout de la référence quand c'est le 1er enregistrement de l'annee concernée
	@Test
	public synchronized void addReferenceTest_whenNoSequenceExistsYet() {
	EcritureComptable vEcritureComptable;
	    vEcritureComptable = new EcritureComptable();
	    vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
	    vEcritureComptable.setDate(new Date());
	    vEcritureComptable.setLibelle("Libelle");
	    vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1), 
	                                                                                 null, new BigDecimal(123),
	                                                                                 null));
	    vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
	                                                                                 null, null,
	                                                                                 new BigDecimal(123)));
	    manager.addReference(vEcritureComptable);
	    assertEquals("AC-2019/00001", vEcritureComptable.getReference());
	}
    
    //RG_Compta_5 : teste l'ajout de la référence quand il existe déjà des enregistrements pour l'année concernée
    @Test
    public synchronized void addReferenceTest_whenLastSequenceExists() {
    	EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
    	Calendar calendar = Calendar.getInstance();
    	calendar.set(2016, 11, 03);
        vEcritureComptable.setDate(calendar.getTime());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1), 
                                                                                 null, new BigDecimal(123),
                                                                                 null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
                                                                                 null, null,
                                                                                 new BigDecimal(123)));
        manager.addReference(vEcritureComptable);
        assertEquals("AC-2016/00041", vEcritureComptable.getReference());
    }
}
