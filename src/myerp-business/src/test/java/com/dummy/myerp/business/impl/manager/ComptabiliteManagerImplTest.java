package com.dummy.myerp.business.impl.manager;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

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

    @Test
    public void checkEcritureComptableUnit() throws Exception {
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

    @Test
    public void checkEcritureComptableUnitViolation() throws Exception {
    	expectedEx.expect(FunctionalException.class);
        expectedEx.expectMessage("L'écriture comptable ne respecte pas les règles de gestion.");
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        manager.checkEcritureComptableUnit(vEcritureComptable);
        
    }

    @Test
    public void checkEcritureComptableUnitRG2() throws Exception {
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
       
      
 // ==================== Test de la méthode AddReference ====================
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
