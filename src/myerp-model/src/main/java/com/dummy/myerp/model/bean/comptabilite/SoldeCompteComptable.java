package com.dummy.myerp.model.bean.comptabilite;

import java.math.BigDecimal;

import com.dummy.myerp.model.validation.constraint.MontantComptable;

public class SoldeCompteComptable {
    /** The Debit. */
    @MontantComptable
    private BigDecimal valeur;
    
    /**  Indication du solde débiteur ou créditeur */
    private String libelle;
    
    // ==================== Constructeurs ====================
    
	public SoldeCompteComptable() {
	} 
	
	public SoldeCompteComptable(BigDecimal valeur, String libelle) {
		this.valeur = valeur;
		this.libelle = libelle;
	} 
    
    
	// ==================== Getters/Setters ====================

	public BigDecimal getValeur() {
		return valeur;
	}

	public void setValeur(BigDecimal valeur) {
		this.valeur = valeur;
	}

	  public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	// ==================== Méthodes ====================
    @Override
    public String toString() {
        final StringBuilder vStB = new StringBuilder(this.getClass().getSimpleName());
        final String vSEP = ", ";
        vStB.append("{")
            .append("valeur=").append(valeur)
            .append(vSEP).append("libelle='").append(libelle).append('\'')
            .append("}");
        return vStB.toString();
    }
    
    
}
