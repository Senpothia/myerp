package com.dummy.myerp.testbusiness.business;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.dummy.myerp.model.bean.comptabilite.*;
import com.dummy.myerp.technical.exception.FunctionalException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;

import com.dummy.myerp.business.impl.manager.ComptabiliteManagerImpl;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertThat;

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

	/**
	 * Recherche une écriture comptable dans la liste des écritures
	 * en fonction d'un journal comptable
	 *
	 * @param code_journal
	 * @param ecritures
	 * @return EcritureComptable
	 */


	private EcritureComptable findEcriture(String code_journal, String libelle_ecriture, List<EcritureComptable> ecritures){

		EcritureComptable ecriture = null;
		boolean finded = false;
		int i = 0;
		while (!finded){
			ecriture = ecritures.get(i);
			String code = ecriture.getJournal().getCode();
			String libelle = ecriture.getLibelle();
			if (code.equals(code_journal)
			&& libelle.equals((libelle_ecriture))){
				finded = true;
			}
			i++;
		}
		return ecriture;
	}



	private JournalComptable findJournal(List<JournalComptable> journaux, String code_journal){

		JournalComptable journal = null;
		boolean finded = false;
		int i = 0;
		while (!finded){

			if (journaux.get(i).getCode().equals(code_journal)) {
				journal = journaux.get(i);
				finded = true;
			}
			i++;
		}
		return  journal;
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
	public void r1_obtenirListeEcrituresComptable() {

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
	public  void r2_getLastSequence(){

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
	public void  r3_updateSequenceEcritureComptableTest(){

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
	public void r4_obtenirDerniereValeurSequenceTest(){

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
	public void r5_saveSequenceTest(){

		SequenceEcritureComptable sequence = new SequenceEcritureComptable();
		sequence.setDerniereValeur(1);
		sequence.setAnnee(2000);
		sequence.setJournalCode("AC");
		manager.insertSequence(sequence);
	}

	/**
	 * Permet de vérifier mise à jour de l'écriture compatble avec la référnce calculée
	 *
	 */

	@Test
	public void r6_updateEcritureComptableTest() throws FunctionalException {
		/**
		 * Erreur relevée à la ligne 63 fichier sqlContext.xml: manquait virgule après debit
		 */
		List<EcritureComptable> ecritures = manager.getListEcritureComptable();
		EcritureComptable ecriture = findEcriture("AC", "Cartouches d’imprimante", ecritures);
		String reference = "YY-2000/11111";
		ecriture.setReference(reference);
		manager.updateEcritureComptable(ecriture);
		ecritures = manager.getListEcritureComptable();
		ecriture = findEcriture("AC", "Cartouches d’imprimante", ecritures);
		Assert.assertEquals("YY-2000/11111", ecriture.getReference());

	}

	/**
	 * Test de la méthode chargée d'ajouter une référence à une Ecriture comptable
	 * dans le cas d'une écriture dont la référence n'est pas nulle
	 *
	 */

	@Test
	public void r7_addRefenceRefNotNullTest(){

		List<EcritureComptable> ecritures = manager.getListEcritureComptable();
		EcritureComptable ecriture = findEcriture("BQ", "Paiement Facture F110001", ecritures);
		String reference = ecriture.getReference();
		String[] splitTab = reference.split("/");
		int val = Integer.parseInt(splitTab[1]);
		//val++;
		String convertVal = String.valueOf(val);
		while(convertVal.length()<5){
			convertVal = "0" + convertVal;
		}
		String expectedRef = splitTab[0] + "/" + convertVal;
		manager.addReference(ecriture);
		ecritures = manager.getListEcritureComptable();
		ecriture = findEcriture("BQ", "Paiement Facture F110001", ecritures);
		reference = ecriture.getReference();
		Assert.assertEquals(expectedRef,reference);
	}

	/**
	 * Test de la méthode chargée d'ajouter une référence à une Ecriture comptable
	 * dans le cas d'une écriture dont la référence est nulle
	 *
	 */

	@Test
	public void r8_addReferenceRefNullTest() throws FunctionalException {

		List<JournalComptable> journaux = manager.getListJournalComptable();
		JournalComptable journal = findJournal(journaux,"AC");
		EcritureComptable ecriture = new EcritureComptable();
		List<LigneEcritureComptable> lignes = ecriture.getListLigneEcriture();
		ecriture.setJournal(journal);
		ecriture.setLibelle("test addRefence()");
		ecriture.setDate(new Date());
		lignes.add(new LigneEcritureComptable(new CompteComptable(606),null, new BigDecimal(1000),null));
		lignes.add(new LigneEcritureComptable(new CompteComptable(606),null, null,new BigDecimal(1000)));
		manager.addReference(ecriture);
		//manager.insertEcritureComptable(ecriture);
	}

	/**
	 * On vérifie qu'une ecriture comptable conforme aux règles de gestion ne retourne pas
	 * d'exception liée à sa référence
	 * @throws Exception
	 */
	@Test//(expected = FunctionalException.class)
	public void r9_checkEcritureComptableRefUniqueRefConforme_RG6() throws Exception {

		//TODO (perso)
		List<EcritureComptable> ecritures = manager.getListEcritureComptable();
		EcritureComptable ecriture = ecritures.get(0);
		manager.checkEcritureComptable(ecriture);

	}

	@Test(expected = FunctionalException.class)
	public void r91_checkEcritureComptableRefUniqueRefExistante_RG6() throws Exception {

		List<EcritureComptable> ecritures = manager.getListEcritureComptable();
		EcritureComptable ecriture = ecritures.get(0);
		EcritureComptable ecritureAuxiliaire = ecritures.get(1);
		ecriture.setReference(ecritureAuxiliaire.getReference());
		manager.checkEcritureComptable(ecriture);

	}

	@Test
	public void r92_insertEcritureComptableTest(){

		List<EcritureComptable> ecritures = manager.getListEcritureComptable();
		int nombreInitialEcritures = ecritures.size();

		EcritureComptable ecriture = new EcritureComptable();
		JournalComptable journal
				= new JournalComptable("AC", "Achat");
		ecriture.setJournal(journal);
		ecriture.setDate(new Date());
		ecriture.setReference("AC-2020/00100");
		ecriture.setLibelle("Test insertion");
		ecriture.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(401),null, new BigDecimal(123),null));
		ecriture.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(411),null, null,new BigDecimal(123)));

		int tailleDeLalisteAvantInsert = manager.getListEcritureComptable().size();
		try {
			manager.insertEcritureComptable(ecriture);
		} catch (FunctionalException e) {
			e.printStackTrace();
		}
		List<EcritureComptable> ecrituresApresInsertion = manager.getListEcritureComptable();
		int nombreEcrituresApres = ecrituresApresInsertion.size();
		Assert.assertEquals(nombreInitialEcritures+1, nombreEcrituresApres);

	}

	@Test
	public void r93_deleteEcritureComptableTest(){

		List<EcritureComptable> ecritures = manager.getListEcritureComptable();
		int nombreInitialEcritures = ecritures.size();
		EcritureComptable ecriture = findEcriture("AC", "Test insertion", ecritures);
		manager.deleteEcritureComptable(ecriture.getId());
		List<EcritureComptable> ecrituresApres = manager.getListEcritureComptable();
		int nombreFinalEcritures = ecrituresApres.size();
		Assert.assertEquals(nombreInitialEcritures-1, nombreFinalEcritures);

	}

}
