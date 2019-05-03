package com.dummy.myerp.business.impl.manager;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import com.dummy.myerp.consumer.dao.contrat.ComptabiliteDao;
import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.model.bean.comptabilite.LigneEcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.SequenceEcritureComptable;
import com.dummy.myerp.technical.exception.NotFoundException;

public class ComptabiliteDaoMock implements ComptabiliteDao{
	
	public void initMock() {
	}
	
	@Override
	public List<CompteComptable> getListCompteComptable() {
		return null;
	}

	@Override
	public List<JournalComptable> getListJournalComptable() {
		return null;
	}

	@Override
	public JournalComptable getJournalComptableByCode(String pCode) throws NotFoundException {
		return null;
	}

	@Override
	public SequenceEcritureComptable getLastSequenceByCodeAndYear(String pCode, Integer pAnnee)
			throws NotFoundException {
		if (pCode.equals("AC") && pAnnee.equals(2016)) {
			return new SequenceEcritureComptable(2016, new JournalComptable("AC","Achat"), 40);
		} else {
			throw new NotFoundException("Aucun enregistrement pour le journal pour l'année concernée");
		}
	}

	@Override
	public void insertSequenceEcritureComptable(String pJournalCode, Integer pAnnee, Integer pDerniereValeur) {
		
	}

	@Override
	public void updateSequenceEcritureComptable(String pJournalCode, Integer pAnnee, Integer pDerniereValeur) {
		
	}

	@Override
	public List<EcritureComptable> getListEcritureComptable() {
		return null;
	}

	@Override
	public EcritureComptable getEcritureComptable(Integer pId) throws NotFoundException {
		return null;
	}

	@Override
	public EcritureComptable getEcritureComptableByRef(String pReference) throws NotFoundException {
		EcritureComptable vEcritureComptable = new EcritureComptable();
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
		if (pReference.equals("AC-2016/00001")) {
			return vEcritureComptable;
		} else  {
			throw new NotFoundException("EcritureComptable non trouvée : reference=" + pReference);
		}
	}

	@Override
	public void loadListLigneEcriture(EcritureComptable pEcritureComptable) {
		
	}

	@Override
	public void insertEcritureComptable(EcritureComptable pEcritureComptable) {
		
	}

	@Override
	public void updateEcritureComptable(EcritureComptable pEcritureComptable) {
		
	}

	@Override
	public void deleteEcritureComptable(Integer pId) {
		
	}



}
