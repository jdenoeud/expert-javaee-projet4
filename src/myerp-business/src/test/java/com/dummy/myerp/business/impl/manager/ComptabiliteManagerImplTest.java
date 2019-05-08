package com.dummy.myerp.business.impl.manager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.model.bean.comptabilite.LigneEcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.SoldeCompteComptable;
import com.dummy.myerp.technical.exception.FunctionalException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/**/comptManagerImplTest-context.xml" })
public class ComptabiliteManagerImplTest {

    private ComptabiliteManagerImpl manager = new ComptabiliteManagerImpl();
//    @Autowired
//    @Qualifier(value="comptabiliteDaoMock")
//    private ComptabiliteDaoMock dao;
//
//    @Before
//    public void initTest() {
//        this.dao.initMock();
//    }
    @Rule 
    public ExpectedException expectedEx = ExpectedException.none();
    
    
 // ==================== Test de la méthode AddReference ====================
    
    // RG_Compta_1 : teste que la solde calculé soit exact et que le solde soit bien débiteur 
    // dans le cas où plusieurs lignes d'écriture existent pour ce compte
    
    @Test
    public void getSoldeCompteComptableTest_whenCompteWithLigneEcriture_returnSucess() {
    	CompteComptable compte = new CompteComptable(512, "Banque");
    	
    	SoldeCompteComptable compteActual = manager.getSoldeCompteComptable(compte.getNumero());
    	assertEquals(new BigDecimal("2947.26"), compteActual.getValeur());
    	assertEquals("Solde débiteur",compteActual.getLibelle());
    	
    }
    
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
    

// ==================== Test de la méthode checkEcritureComptableUnit ====================
    //Vérifie que les champs de l'écriture comptable respectent les contraintes unitaires (annotations)
    @Test
    public void checkEcritureComptableUnit_contraintes_respectees() throws Exception {
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.setReference("AC-2019/00001");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                                                                                 null, new BigDecimal(123),
                                                                                 null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
                                                                                 null, null,
                                                                                 new BigDecimal(123)));
        manager.checkEcritureComptableUnit(vEcritureComptable);
    }

    //Vérifie que les champs de l'écriture comptable respectent les contraintes unitaires (annotations)
    @Test
    public void checkEcritureComptableUnitViolation_contraintes_non_respectees() throws Exception {
    	expectedEx.expect(FunctionalException.class);
        expectedEx.expectMessage("L'écriture comptable ne respecte pas les règles de gestion.");
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        manager.checkEcritureComptableUnit(vEcritureComptable);
        
    }
    
    //RG_Compta_4 : Vérifie que le montant des lignes d'écriture sont signés et peuvent être négatifs
    @Test
    public void checkEcritureComptableUnit_montant_ligne_signes() throws Exception {
//    	expectedEx.expect(FunctionalException.class);
//    	expectedEx.expectMessage("L'écriture comptable ne respecte pas les règles de gestion.");
        EcritureComptable vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.setReference("AC-2019/00001");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                                                                                 null, new BigDecimal("-123"),
                                                                                 null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
                                                                                 null, null,
                                                                                 new BigDecimal("-123")));       
        manager.checkEcritureComptableUnit(vEcritureComptable);
     }
    
    //RG_Compta_7 : Vérifie que le montant des lignes d'écriture ne comportent pas plus de 2 chiffres après la virgule
    @Test
    public void checkEcritureComptableUnitViolation_nb_chiffres_apres_virgules_depasses() throws Exception {
    	expectedEx.expect(FunctionalException.class);
    	expectedEx.expectMessage("L'écriture comptable ne respecte pas les règles de gestion.");
        EcritureComptable vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.setReference("AC-2019/00001");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                                                                                 null, new BigDecimal("123.564").setScale(3, BigDecimal.ROUND_HALF_UP),
                                                                                 null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
                                                                                 null, null,
                                                                                 new BigDecimal("123.564").setScale(3, BigDecimal.ROUND_HALF_UP)));       
        manager.checkEcritureComptableUnit(vEcritureComptable);
     }

    // RG_Compta_2 : Vérifie que l'écriture comptable est équilibrée
    @Test
    public void checkEcritureComptableUnitRG2_ecriture_non_equilibree() throws Exception {
    	expectedEx.expect(FunctionalException.class);
        expectedEx.expectMessage("L'écriture comptable n'est pas équilibrée.");
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
                                                                                 new BigDecimal(1234)));
        manager.checkEcritureComptableUnit(vEcritureComptable);
    }

    @Test (expected = FunctionalException.class)
    public void checkEcritureComptableUnitRG3() throws Exception {
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                                                                                 null, new BigDecimal(123),
                                                                                 null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                                                                                 null, new BigDecimal(123),
                                                                                 null));
        manager.checkEcritureComptableUnit(vEcritureComptable);
    }
    
    @Test
    public void checkEcritureComptableUnitRG5_GoodYearInReference() throws Exception {  
	  	EcritureComptable vEcritureComptable;
	  	vEcritureComptable = new EcritureComptable();
	  	vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
	  	Calendar calendar = new GregorianCalendar(2016, Calendar.DECEMBER, 31);
	  	vEcritureComptable.setDate(calendar.getTime());
	  	vEcritureComptable.setLibelle("Cartouches d'imprimantes");
	  	vEcritureComptable.setReference("AC-2016/00001");
	  	vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
               null, new BigDecimal(123),
               null));
	  	vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
               null, null,
               new BigDecimal(123)));
	  	try {
	  		manager.checkEcritureComptableUnit(vEcritureComptable);
	  	} catch (Exception e){
	  		fail("Exception levée : "+e.getMessage());
	  	}
	  	
   }	
    @Test
    public void checkEcritureComptableUnitRG5_wrongYear() throws Exception {
    	expectedEx.expect(FunctionalException.class);
        expectedEx.expectMessage("L'année de la référence ne correspond pas à la date de l'écriture");
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.setReference("AC-2016/00001");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, new BigDecimal(123),
                null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
                    null, null,
                    new BigDecimal(123)));
        manager.checkEcritureComptableUnit(vEcritureComptable);
     } 
    @Test
    public void checkEcritureComptableUnitRG5_wrongYearBis() throws Exception {
       expectedEx.expect(FunctionalException.class);
        expectedEx.expectMessage("L'année de la référence ne correspond pas à la date de l'écriture");
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.setReference("AC-2016/02019");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                    null, new BigDecimal(123),
                    null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
                    null, null,
                    new BigDecimal(123)));
        manager.checkEcritureComptableUnit(vEcritureComptable);
    }
        
    @Test
    public void checkEcritureComptableUnitRG5_wrongJournalCode() throws Exception {
    	expectedEx.expect(FunctionalException.class);
    	expectedEx.expectMessage("Le code journal de la référence ne correspond pas au journal de l'écriture");
    	EcritureComptable vEcritureComptable;
    	vEcritureComptable = new EcritureComptable();
    	vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.setReference("VE-2019/00001");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                    null, new BigDecimal(123),
                    null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
                    null, null,
                    new BigDecimal(123)));
        manager.checkEcritureComptableUnit(vEcritureComptable);
    }
       
// ==================== Test de la méthode checkEcritureComptableContext ====================
    //RG_Compta_6 : teste l'unicité de la référence comptable avec une écriture comptable qui existe déjà
    @Test
    public void checkEcritureComptableContext_whenReferenceAlreadyExists() throws Exception {  
    	expectedEx.expect(FunctionalException.class);
    	expectedEx.expectMessage("Une autre écriture comptable existe déjà avec la même référence.");
    	EcritureComptable vEcritureComptable;
    	vEcritureComptable = new EcritureComptable();
    	vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
	  	Calendar calendar = new GregorianCalendar(2016, Calendar.DECEMBER, 31);
    	vEcritureComptable.setDate(calendar.getTime());
    	vEcritureComptable.setLibelle("Cartouches d'imprimantes");
    	vEcritureComptable.setReference("AC-2016/00001");
    	vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
               null, new BigDecimal(123),
               null));
    	vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
               null, null,
               new BigDecimal(123)));

    	manager.checkEcritureComptable(vEcritureComptable);
    }
    
    //RG_Compta_6 : teste l'unicité de la référence comptable avec une écriture comptable qui existe déjà  mais avec un id différent   
    @Test
    public void checkEcritureComptableContext_whenReferenceExists_but_Id_is_different() throws Exception {  
    	expectedEx.expect(FunctionalException.class);
    	expectedEx.expectMessage("Une autre écriture comptable existe déjà avec la même référence.");
    	EcritureComptable vEcritureComptable;
    	vEcritureComptable = new EcritureComptable();
    	vEcritureComptable.setId(10);
    	vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
	  	Calendar calendar = new GregorianCalendar(2016, Calendar.DECEMBER, 31);
    	vEcritureComptable.setDate(calendar.getTime());
    	vEcritureComptable.setLibelle("Cartouches d'imprimantes");
    	vEcritureComptable.setReference("AC-2016/00001");
    	vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
               null, new BigDecimal(123),
               null));
    	vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
               null, null,
               new BigDecimal(123)));

    	manager.checkEcritureComptable(vEcritureComptable);
    }  
   
    //RG_Compta_6 : teste l'unicité de la référence comptable avec une écriture comptable qui n'existe pas encore
    @Test
    public void checkEcritureComptable_whenReferenceDoesNotExist() throws Exception {  
    	EcritureComptable vEcritureComptable;
    	vEcritureComptable = new EcritureComptable();
    	vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
    	vEcritureComptable.setDate(new Date());
    	vEcritureComptable.setLibelle("Libelle");
    	vEcritureComptable.setReference("AC-2019/00245");
    	vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
               null, new BigDecimal(123),
               null));
    	vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
               null, null,
               new BigDecimal(123)));
    	try {
    		manager.checkEcritureComptable(vEcritureComptable);
    	} catch (Exception e) {
    		fail("Exception levée : "+ e.getMessage());
    	}
    	
    }
}
