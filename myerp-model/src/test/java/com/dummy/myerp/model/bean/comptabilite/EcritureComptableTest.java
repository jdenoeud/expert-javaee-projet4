package com.dummy.myerp.model.bean.comptabilite;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.junit.Assert;
import org.junit.Test;


public class EcritureComptableTest {

    private LigneEcritureComptable createLigne(Integer pCompteComptableNumero, String pDebit, String pCredit) {
        BigDecimal vDebit = pDebit == null ? null : new BigDecimal(pDebit);
        BigDecimal vCredit = pCredit == null ? null : new BigDecimal(pCredit);
        String vLibelle = ObjectUtils.defaultIfNull(vDebit, BigDecimal.ZERO)
                                     .subtract(ObjectUtils.defaultIfNull(vCredit, BigDecimal.ZERO)).toPlainString();
        LigneEcritureComptable vRetour = new LigneEcritureComptable(new CompteComptable(pCompteComptableNumero),
                                                                    vLibelle,
                                                                    vDebit, vCredit);
        return vRetour;
    }

    @Test
    public void isEquilibree_whenEcritureIsEquilibree() {
        EcritureComptable vEcriture;
        vEcriture = new EcritureComptable();

        vEcriture.setLibelle("Equilibrée");
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "200.50", null));
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "100.50", "33"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, null, "301"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "40", "7"));
        
        Assert.assertTrue(vEcriture.toString(), vEcriture.isEquilibree());
    }
    
    @Test
    public void isNotEquilibree_whenEcritureIsNotEquilibree() {
        EcritureComptable vEcriture;
        vEcriture = new EcritureComptable();

        vEcriture.getListLigneEcriture().clear();
        vEcriture.setLibelle("Non équilibrée");
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "10", null));
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "20", "1"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, null, "30"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "1", "2"));
        
        Assert.assertFalse(vEcriture.toString(), vEcriture.isEquilibree());
    }
    
    @Test
    public void getTotalDebitTest() {
    	 EcritureComptable vEcriture;
         vEcriture = new EcritureComptable();

         vEcriture.setLibelle("Equilibrée");
         vEcriture.getListLigneEcriture().add(this.createLigne(1, "10", null));
         vEcriture.getListLigneEcriture().add(this.createLigne(1, "20", "1"));
         vEcriture.getListLigneEcriture().add(this.createLigne(2, null, "30"));
         vEcriture.getListLigneEcriture().add(this.createLigne(2, "1", "2"));
         
         Assert.assertEquals("Somme des débits incorrecte" , new BigDecimal(31),vEcriture.getTotalDebit());
    }
    
    @Test
    public void getTotalCreditTest() {
    	 EcritureComptable vEcriture;
         vEcriture = new EcritureComptable();

         vEcriture.setLibelle("Equilibrée");
         vEcriture.getListLigneEcriture().add(this.createLigne(1, "10", null));
         vEcriture.getListLigneEcriture().add(this.createLigne(1, "20", "1"));
         vEcriture.getListLigneEcriture().add(this.createLigne(2, null, "30"));
         vEcriture.getListLigneEcriture().add(this.createLigne(2, "1", "2"));
         
         Assert.assertEquals("Somme des crédits incorrecte" , new BigDecimal(33),vEcriture.getTotalCredit());
    }
    
    @Test
    public void getInListByReference_whenEcritureIsInTheListe() {
    	List<EcritureComptable> ecritures = new ArrayList<EcritureComptable>();
    	
    	EcritureComptable vEcritureComptable1 = new EcritureComptable();
    	vEcritureComptable1.setJournal(new JournalComptable("AC", "Achat"));
	    vEcritureComptable1.setLibelle("Libelle");
        vEcritureComptable1.setDate(new Date());
        vEcritureComptable1.setReference("AC-2016/00001");
        ecritures.add(vEcritureComptable1);
        
        EcritureComptable vEcritureComptable2 = new EcritureComptable();
    	vEcritureComptable2.setJournal(new JournalComptable("VE", "Vente"));
	    vEcritureComptable2.setLibelle("Libelle");
        vEcritureComptable2.setReference("VE-2016/00002");
        ecritures.add(vEcritureComptable2);
        
        EcritureComptable ecritureActual = EcritureComptable.getInListByReference(ecritures, "VE-2016/00002");
        assertEquals(vEcritureComptable2.toString(), ecritureActual.toString()); 
    }   
 
}
