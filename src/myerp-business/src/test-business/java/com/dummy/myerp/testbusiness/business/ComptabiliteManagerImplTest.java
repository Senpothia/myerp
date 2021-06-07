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
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:bootstrapContext.xml"})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@TransactionConfiguration(defaultRollback = true)
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

	/**
	 *Test la méthode permettant de récupérer la liste des ecritures comptables
	 *
	 */
	@Test
	public void obtenirListeEcrituresComptable() {

		manager = new ComptabiliteManagerImpl();
		List<JournalComptable> journaux = manager.getListJournalComptable();
		System.out.println("Taille liste: " + journaux.size());
		Assert.assertEquals(journaux.size(), 4);

	}

	/**
	 * Test de la méthode permettant de retrouver une séquence comptable
	 * à partir d'un journal comptable et d'une année
	 */
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

	/**
	 * Test de la mise à jour d'une séquence comptable en base de données
	 */
	@Test
	public void  updateSequenceEcritureComptableTest(){

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
		System.out.println("seq: Code_journal: " + sequence.getJournalCode());
		System.out.println("seq: année: " + sequence.getAnnee());
		System.out.println("seq: dernière valeur: " + sequence.getDerniereValeur());
		int derniere_valeur = sequence.getDerniereValeur();
		derniere_valeur++;
		manager.updateSequence(derniere_valeur, sequence);
		SequenceEcritureComptable sequenceAjout = manager.findSequence(journal, 2016);
		int derniere_valeur_a_jour = sequenceAjout.getDerniereValeur();
		Assert.assertEquals(derniere_valeur++,derniere_valeur_a_jour);

	}

	/**
	 * Test de la méthode obtenirDerniereValeurSequence()
	 * Cette méthode retourne directement la dernière valeur de la séquence
	 * sans remonter la séquence concernée
	 */
	@Test
	public void obtenirDerniereValeurSequenceTest(){

		String journal_code = "AC";
		int annee = 2016;
		int derniere_valeur = manager.obtenirDerniereValeurSequence(journal_code, annee);
		System.out.println("Valeur :" + derniere_valeur);
		int expected_derniere_valeur;
		List<JournalComptable> journaux = manager.getListJournalComptable();
		JournalComptable journal = null;
		boolean finded = false;
		int i = 0;
		while (!finded){

			if (journaux.get(i).getCode().equals(journal_code)) {
				journal = journaux.get(i);
				finded = true;
			}
			i++;
		}
		SequenceEcritureComptable sequenceEcritureComptable = manager.findSequence(journal, 2016);
		expected_derniere_valeur = sequenceEcritureComptable.getDerniereValeur();
		Assert.assertEquals(expected_derniere_valeur, derniere_valeur);

	}

	/**
	 * Test l'enregistrement d'une nouvelle séquence comptable
	 */
	@Test
	public void saveSequenceTest(){

		SequenceEcritureComptable sequence = new SequenceEcritureComptable();
		sequence.setDerniereValeur(1);
		sequence.setAnnee(2000);
		sequence.setJournalCode("AC");
		manager.insertSequence(sequence);
	}

	//TODO (perso): vérifier mise à jour de l'écriture compatble avec la référnce calculée

}
