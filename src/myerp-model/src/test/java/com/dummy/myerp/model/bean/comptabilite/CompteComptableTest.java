package com.dummy.myerp.model.bean.comptabilite;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CompteComptableTest {
	
	// ADD
	
	private CompteComptable compte1;
	private CompteComptable compte2;
	private CompteComptable compte3;
	private CompteComptable compte4;
	private CompteComptable compte5;
	
	private List<CompteComptable> listeComptes1;
	private List<CompteComptable> listeComptes2;
	private List<CompteComptable> listeComptes3;
	
	@Before
	public void setUp() {
		
		
		compte1 = new CompteComptable(1, "compte1");
		compte2 = new CompteComptable(2, "compte2");
		compte3 = new CompteComptable(3, "compte3");
		compte4 = new CompteComptable(4, "compte4");
		compte5 = new CompteComptable(5, "compte5");
		
		listeComptes1 = new ArrayList<>();
		listeComptes2 = new ArrayList<>();
		listeComptes3 = new ArrayList<>();
		
		
		listeComptes1.add(compte1);
		listeComptes1.add(compte2);
		listeComptes1.add(compte3);
		
		listeComptes2.add(compte4);
		listeComptes2.add(compte5);
		listeComptes2.add(compte2);
		
		listeComptes3.add(compte2);
		listeComptes3.add(compte3);
		listeComptes3.add(compte4);
		
		
	}
	
	@Test
	public void getByNumberTrue() {
		
		CompteComptable compte =  CompteComptable.getByNumero(listeComptes1, 1);
		Assert.assertEquals(compte1, compte);
	
	}
	
	
	@Test
	public void getByNumberFalse() {
		
		CompteComptable compte =  CompteComptable.getByNumero(listeComptes1, 5);
		Assert.assertNull(compte);
	
	}
	
	
	@Test
	public void getByNumberNull() {
		
		CompteComptable compte =  CompteComptable.getByNumero(listeComptes1, 5);
		Assert.assertNull(compte);
	
	}
	
	

}
