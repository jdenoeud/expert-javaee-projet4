package com.dummy.myerp.business.impl.manager;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.transaction.TransactionStatus;

import com.dummy.myerp.business.contrat.manager.ComptabiliteManager;
import com.dummy.myerp.business.impl.AbstractBusinessManager;
import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.model.bean.comptabilite.LigneEcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.SequenceEcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.SoldeCompteComptable;
import com.dummy.myerp.technical.exception.FunctionalException;
import com.dummy.myerp.technical.exception.NotFoundException;


/**
 * Comptabilite manager implementation.
 */
public class ComptabiliteManagerImpl extends AbstractBusinessManager implements ComptabiliteManager {

    // ==================== Attributs ====================
	/** Logger Log4j pour la classe */
    private static final Logger LOGGER = LogManager.getLogger(ComptabiliteManagerImpl.class);

    // ==================== Constructeurs ====================
    /**
     * Instantiates a new Comptabilite manager.
     */
    public ComptabiliteManagerImpl() {
    }


    // ==================== Getters/Setters ====================
    @Override
    public List<CompteComptable> getListCompteComptable() {
        return getDaoProxy().getComptabiliteDao().getListCompteComptable();
    }


    @Override
    public List<JournalComptable> getListJournalComptable() {
        return getDaoProxy().getComptabiliteDao().getListJournalComptable();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<EcritureComptable> getListEcritureComptable() {
        return getDaoProxy().getComptabiliteDao().getListEcritureComptable();
    }
    
    // ===== RG_Compta_1 ==== 
    // Le solde d'un compte comptable est égal à la somme des montants au débit diminiuées de la somme des
    // montants au crédit. Si le résultat est positif, le solde est dit "débiteur", 
    // si le résultat est négatif, le solde est dit"créditeur"
    
	@Override
	public SoldeCompteComptable getSoldeCompteComptable(Integer pNumero) {
		SoldeCompteComptable vSolde = new SoldeCompteComptable();
		List<LigneEcritureComptable> lignes = getDaoProxy().getComptabiliteDao().getListLigneEcritureComptable(pNumero);
		BigDecimal total = new BigDecimal("0");
		for (LigneEcritureComptable l : lignes) {
			if (l.getDebit() != null) {
                total = total.add(l.getDebit());
            } else {
                total = total.subtract(l.getCredit());
            }
		}
		vSolde.setValeur(total);
		if (total.compareTo(BigDecimal.ZERO) > 0 ) {
			vSolde.setLibelle("Solde débiteur");
		} else if (total.compareTo(BigDecimal.ZERO) < 0) {
			vSolde.setLibelle("Solde créditeur");
		} else {
			vSolde.setLibelle("Solde nul");
		}
		return vSolde;
	}
    
    
    /**
     * {@inheritDoc}
     */
	// TODO à tester ==> FAIT
    @Override
    public synchronized void addReference(EcritureComptable pEcritureComptable) {
        // TODO à implémenter ==> FAIT
        // Bien se réferer à la JavaDoc de cette méthode !
        /* Le principe :
                1.  Remonter depuis la persitance la dernière valeur de la séquence du journal pour l'année de l'écriture
                    (table sequence_ecriture_comptable)
                2.  * S'il n'y a aucun enregistrement pour le journal pour l'année concernée :
                        1. Utiliser le numéro 1.
                    * Sinon :
                        1. Utiliser la dernière valeur + 1
                3.  Mettre à jour la référence de l'écriture avec la référence calculée (RG_Compta_5)
                4.  Enregistrer (insert/update) la valeur de la séquence en persistance
                    (table sequence_ecriture_comptable)
         */
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(pEcritureComptable.getDate());
    	Integer annee = calendar.get(Calendar.YEAR);
    	String reference = "";
    	SequenceEcritureComptable derniereSequence = null;
    	Integer derniereValeur = 1;
    	try {
			derniereSequence = getDaoProxy().getComptabiliteDao().getLastSequenceByCodeAndYear(pEcritureComptable.getJournal().getCode(), annee);
			derniereValeur = derniereSequence.getDerniereValeur()+1;
			getDaoProxy().getComptabiliteDao().updateSequenceEcritureComptable(derniereSequence.getJournal().getCode(), derniereSequence.getAnnee(), derniereValeur);
		} catch (NotFoundException e) {
			getDaoProxy().getComptabiliteDao().insertSequenceEcritureComptable(pEcritureComptable.getJournal().getCode(), annee, 1);
		}
    	String numSequence = "";
    	if (derniereValeur<10) {
    		numSequence = "0000"+Integer.toString(derniereValeur);
    	} else if (derniereValeur<100) {
    		numSequence = "000"+Integer.toString(derniereValeur);
    	} else if (derniereValeur<1000) {
    		numSequence = "00"+Integer.toString(derniereValeur);
    	} else if (derniereValeur<10000) {
    		numSequence = "0"+Integer.toString(derniereValeur);
    	} else {
    		numSequence = Integer.toString(derniereValeur);
    	}
    	
    	reference = pEcritureComptable.getJournal().getCode()+"-"+Integer.toString(annee)+"/"+numSequence;
    	pEcritureComptable.setReference(reference);
    	
    }

    /**
     * {@inheritDoc}
     */
    // TODO à tester ==> FAIT
    @Override
    public void checkEcritureComptable(EcritureComptable pEcritureComptable) throws FunctionalException {
        this.checkEcritureComptableUnit(pEcritureComptable);
        this.checkEcritureComptableContext(pEcritureComptable);
    }
    /**
     * Vérifie que l'Ecriture comptable respecte les règles de gestion unitaires,
     * c'est à dire indépendemment du contexte (unicité de la référence, exercie comptable non cloturé...)
     *
     * @param pEcritureComptable -
     * @throws FunctionalException Si l'Ecriture comptable ne respecte pas les règles de gestion
     */
    // TODO tests à compléter ==> FAIT
    protected void checkEcritureComptableUnit(EcritureComptable pEcritureComptable) throws FunctionalException {
        // ===== Vérification des contraintes unitaires sur les attributs de l'écriture
        // ===== RG_Compta_4 : montants des lignes signés et peuvent être négatif ==> inclus dans la vérification des contraintes ci-dessous
    	// ===== RG_Compta_7 : montants des lignes avec 2 chiffres max après la virgule ==> inclus dans la vérification des contraintes ci-dessous
        Set<ConstraintViolation<EcritureComptable>> vViolations = getConstraintValidator().validate(pEcritureComptable);
        if (!vViolations.isEmpty()) {
        	// Affichage des messages de violation de contraintes 
        	for(ConstraintViolation<EcritureComptable> contraintes : vViolations) {
        		if( contraintes.getMessage().contains("la taille doit être entre 2 et 2147483647")) {
        			ComptabiliteManagerImpl.LOGGER.warn("[RG_Compta_3 non respectée]");
        		} else if(contraintes.getMessage().contains("Valeur numérique hors limite")){
        			ComptabiliteManagerImpl.LOGGER.warn("[RG_Compta_7 non respectée]");
        		}
        		ComptabiliteManagerImpl.LOGGER.warn(contraintes.getRootBeanClass().getSimpleName()+ "." + contraintes.getPropertyPath() + " " + contraintes.getMessage());
        	}
            throw new FunctionalException("L'écriture comptable ne respecte pas les règles de gestion.",
                                          new ConstraintViolationException(
                                              "L'écriture comptable ne respecte pas les contraintes de validation",
                                              vViolations));
        }
      
        // ===== RG_Compta_2 : Pour qu'une écriture comptable soit valide, elle doit être équilibrée
        if (!pEcritureComptable.isEquilibree()) {
        	ComptabiliteManagerImpl.LOGGER.warn("[RG_Compta_2 non respectée]");
			ComptabiliteManagerImpl.LOGGER.warn("Pour qu'une écriture comptable soit valide, elle doit être équilibrée");
            throw new FunctionalException("L'écriture comptable n'est pas équilibrée.");
        }

        // ===== RG_Compta_3 : une écriture comptable doit avoir au moins 2 lignes d'écriture (1 au débit, 1 au crédit)
        int vNbrCredit = 0;
        int vNbrDebit = 0;
        for (LigneEcritureComptable vLigneEcritureComptable : pEcritureComptable.getListLigneEcriture()) {
            if (BigDecimal.ZERO.compareTo(ObjectUtils.defaultIfNull(vLigneEcritureComptable.getCredit(),
                                                                    BigDecimal.ZERO)) != 0) {
                vNbrCredit++;
            }
            if (BigDecimal.ZERO.compareTo(ObjectUtils.defaultIfNull(vLigneEcritureComptable.getDebit(),
                                                                    BigDecimal.ZERO)) != 0) {
                vNbrDebit++;
            }
        }
        // On test le nombre de lignes car si l'écriture à une seule ligne
        //      avec un montant au débit et un montant au crédit ce n'est pas valable
        if (pEcritureComptable.getListLigneEcriture().size() < 2
            || vNbrCredit < 1
            || vNbrDebit < 1) {
        	ComptabiliteManagerImpl.LOGGER.warn("[RG_Compta_3 non respectée]");
			ComptabiliteManagerImpl.LOGGER.warn("Pour qu'une écriture comptable soit valide, elle doit être équilibrée");
            throw new FunctionalException(
                "L'écriture comptable doit avoir au moins deux lignes : une ligne au débit et une ligne au crédit.");
        }
        
        // TODO ===== RG_Compta_5 : Format et contenu de la référence ==> FAIT
        // vérifier que l'année dans la référence correspond bien à la date de l'écriture, idem pour le code journal...
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(pEcritureComptable.getDate());
    	String annee = "-"+Integer.toString(calendar.get(Calendar.YEAR))+"/";
    	if ( pEcritureComptable.getReference() != null) {
    		if (!pEcritureComptable.getReference().contains(annee)) {
    			ComptabiliteManagerImpl.LOGGER.warn("[RG_Compta_5 non respectée]");
    			ComptabiliteManagerImpl.LOGGER.warn("L'année de la référence ne correspond pas à la date de l'écriture");
            	throw new FunctionalException("L'année de la référence ne correspond pas à la date de l'écriture");
            }
            if (!pEcritureComptable.getReference().contains(pEcritureComptable.getJournal().getCode())) {
            	ComptabiliteManagerImpl.LOGGER.warn("[RG_Compta_5 non respectée]");
    			ComptabiliteManagerImpl.LOGGER.warn("Le code journal de la référence ne correspond pas au journal de l'écriture");
            	throw new FunctionalException("Le code journal de la référence ne correspond pas au journal de l'écriture");
            }	
    	}   	
    }

    /**
     * Vérifie que l'Ecriture comptable respecte les règles de gestion liées au contexte
     * (unicité de la référence, année comptable non cloturé...)
     *
     * @param pEcritureComptable -
     * @throws FunctionalException Si l'Ecriture comptable ne respecte pas les règles de gestion
     */
    protected void checkEcritureComptableContext(EcritureComptable pEcritureComptable) throws FunctionalException {
        // ===== RG_Compta_6 : La référence d'une écriture comptable doit être unique
        if (StringUtils.isNoneEmpty(pEcritureComptable.getReference())) {
            try {
                // Recherche d'une écriture ayant la même référence
                EcritureComptable vECRef = getDaoProxy().getComptabiliteDao().getEcritureComptableByRef(
                    pEcritureComptable.getReference());
                // Si l'écriture à vérifier est une nouvelle écriture (id == null),
                // ou si elle ne correspond pas à l'écriture trouvée (id != idECRef),
                // c'est qu'il y a déjà une autre écriture avec la même référence
                if (pEcritureComptable.getId() == null
                    || !pEcritureComptable.getId().equals(vECRef.getId())) {
                	ComptabiliteManagerImpl.LOGGER.warn("[RG_Compta_6 non respectée]");
        			ComptabiliteManagerImpl.LOGGER.warn("La référence d'une écriture comptable doit être unique.");
                    throw new FunctionalException("Une autre écriture comptable existe déjà avec la même référence.");
                }
            } catch (NotFoundException vEx) {          	
                // Dans ce cas, c'est bon, ça veut dire qu'on n'a aucune autre écriture avec la même référence.
            	ComptabiliteManagerImpl.LOGGER.debug("Aucune autre écriture n'a la même référence.");
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertEcritureComptable(EcritureComptable pEcritureComptable) throws FunctionalException {
        this.checkEcritureComptable(pEcritureComptable);
        TransactionStatus vTS = getTransactionManager().beginTransactionMyERP();
        try {
            getDaoProxy().getComptabiliteDao().insertEcritureComptable(pEcritureComptable);
            getTransactionManager().commitMyERP(vTS);
            vTS = null;
        } finally {
            getTransactionManager().rollbackMyERP(vTS);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateEcritureComptable(EcritureComptable pEcritureComptable) throws FunctionalException {
        TransactionStatus vTS = getTransactionManager().beginTransactionMyERP();
        try {
            getDaoProxy().getComptabiliteDao().updateEcritureComptable(pEcritureComptable);
            getTransactionManager().commitMyERP(vTS);
            vTS = null;
        } finally {
            getTransactionManager().rollbackMyERP(vTS);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteEcritureComptable(Integer pId) {
        TransactionStatus vTS = getTransactionManager().beginTransactionMyERP();
        try {
            getDaoProxy().getComptabiliteDao().deleteEcritureComptable(pId);
            getTransactionManager().commitMyERP(vTS);
            vTS = null;
        } finally {
            getTransactionManager().rollbackMyERP(vTS);
        }
    }

}
