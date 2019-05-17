package com.dummy.myerp.model.bean.comptabilite;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class CompteComptableTest {
	
	private List<CompteComptable> comptes = new ArrayList<CompteComptable>();
	
	@Before
	public void init() {
		CompteComptable compte1 = new CompteComptable(401, "Fournisseurs");
		CompteComptable compte2 = new CompteComptable(512, "Banque");
		CompteComptable compte3 = new CompteComptable(706, "Prestations de services");		
		comptes.add(compte1);
		comptes.add(compte2);
		comptes.add(compte3);
	}
	
	@Test
	public void getByNumberTest_whenCompteIsInTheList() {
		CompteComptable compteExpected = new CompteComptable(706, "Prestations de services");		
		CompteComptable compteActual = CompteComptable.getByNumero(comptes, 706);
		assertSame(compteExpected.getLibelle(),compteActual.getLibelle());
	}
	
	@Test
	public void getByNumberTest_whenCompteIsNOTInTheList() {
		CompteComptable compteActual = CompteComptable.getByNumero(comptes, 4456);
		assertNull(compteActual);
	}
	
	@Test
	public void toStringTest() {
		CompteComptable compte = new CompteComptable(706, "Prestations de services");
		assertEquals("CompteComptable{numero=706, libelle='Prestations de services'}",compte.toString());
	}
	
	

}
