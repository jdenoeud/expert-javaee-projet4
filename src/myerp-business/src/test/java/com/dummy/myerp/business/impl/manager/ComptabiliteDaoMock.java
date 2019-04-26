package com.dummy.myerp.business.impl.manager;

import java.util.List;

import com.dummy.myerp.consumer.dao.contrat.ComptabiliteDao;
import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
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
		return null;
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
