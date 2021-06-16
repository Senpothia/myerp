package com.dummy.myerp.model.bean.comptabilite;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class JournalComptableTest {
	
	
	private JournalComptable journal1;
	private JournalComptable journal2;
	private JournalComptable journal3;
	private JournalComptable journal4;
	private JournalComptable journal5;
	
	private List<JournalComptable> liste1;
	private List<JournalComptable> liste2;
	private List<JournalComptable> liste3;
	
	@Before
	public void setUp() {
			
		journal1 = new JournalComptable("axa01", "compte AXA");
		journal2 = new JournalComptable("bnp01", "compte BNP");
		journal3 = new JournalComptable("lcl02", "compte LCL");
		journal4 = new JournalComptable("SG06", "compte Société Générale");
		journal5 = new JournalComptable("BP03", "compte Banque Populaire");
		
		liste1 = new ArrayList<>();
		liste2 = new ArrayList<>();
		liste3 = new ArrayList<>();
		
		liste1.add(journal1);
		liste1.add(journal2);
		liste1.add(journal3);

		liste2.add(journal3);
		liste2.add(journal4);
		liste2.add(journal5);
		
		liste3.add(journal2);
		liste3.add(journal5);

	}
	
	@Test
	public void getByCodeTrue() {
		
		JournalComptable journal = JournalComptable.getByCode(liste1, "axa01");
		Assert.assertEquals(journal1, journal);
	
	}
	
	
	@Test
	public void getByCodeFalse() {
		
		JournalComptable journal = JournalComptable.getByCode(liste2, "axa01");
		Assert.assertNull(journal);
	}
	
	@Test
	public void getByCodeNull() {
		
		JournalComptable journal = JournalComptable.getByCode(liste2, null);
		Assert.assertNull(journal);
	}

	@Test
	public void setLibelleTest(){

		journal1.setLibelle("test libellé");
		Assert.assertEquals("test libellé", journal1.getLibelle());
	}

	@Test
	public void getLibelleTest(){

		String libelle = journal1.getLibelle();
		Assert.assertEquals("compte AXA", journal1.getLibelle());
	}

	@Test
	public void toStringJournalComptabel(){

		String printedJournal = journal1.toString();
		System.out.println(printedJournal);
		Assert.assertEquals("JournalComptable{code='axa01', libelle='compte AXA'}", printedJournal);
	}
	
	
	

}
