package com.dummy.myerp.testbusiness.business;
import java.util.List;

import com.dummy.myerp.model.bean.comptabilite.SequenceEcritureComptable;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;

import com.dummy.myerp.business.impl.manager.ComptabiliteManagerImpl;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:bootstrapContext.xml"})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ComptabiliteManagerImplTest extends BusinessTestCase {
	
	private ComptabiliteManagerImpl manager;

	public  boolean like (SequenceEcritureComptable s1, SequenceEcritureComptable s2) {

		if (s1.getAnnee().equals(s2.getAnnee())
				&& s1.getDerniereValeur().equals(s2.getDerniereValeur())
				&& s1.getJournalCode().equals(s2.getJournalCode())) {

			return true;
		} else {

			return false;
		}
	}

	@Before
	public void setup(){

		manager = new ComptabiliteManagerImpl();
	}
	
	@Test
	public void obtenirListeEcrituresComptable() {

		manager = new ComptabiliteManagerImpl();
		List<JournalComptable> journaux = manager.getListJournalComptable();
		System.out.println("Taille liste: " + journaux.size());
		Assert.assertEquals(journaux.size(), 4);

	}

	@Test
	public  void getLastSequence(){

		SequenceEcritureComptable expectedSequence = new SequenceEcritureComptable();
		expectedSequence.setJournalCode("AC");
		expectedSequence.setAnnee(2016);
		expectedSequence.setDerniereValeur(40);
		List<JournalComptable> journaux = manager.getListJournalComptable();
		System.out.println("taille liste journaux: " + journaux.size());
		int annee = 2016;
		String code = "AC";
		JournalComptable journal = null;
		boolean finded = false;
		int i = 0;
		while (!finded){

				if (journaux.get(i).getCode().equals(code)) {
					journal = journaux.get(i);
					finded = true;
			}
				i++;
		}
		System.out.println("code journal = " + journal.getCode());
		System.out.println("libellé journal = " + journal.getLibelle());

		SequenceEcritureComptable sequence = manager.findSequence(journal, 2016);
		System.out.println(sequence.toString());
		Assert.assertTrue(like(expectedSequence,sequence));
	}





}
