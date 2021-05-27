package com.dummy.myerp.business.impl.manager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.dummy.myerp.business.contrat.BusinessProxy;
import com.dummy.myerp.business.contrat.manager.ComptabiliteManager;
import com.dummy.myerp.business.impl.AbstractBusinessManager;
import com.dummy.myerp.business.impl.BusinessProxyImpl;
import com.dummy.myerp.business.impl.TransactionManager;
import com.dummy.myerp.consumer.dao.contrat.DaoProxy;
import com.dummy.myerp.consumer.dao.impl.db.dao.ComptabiliteDaoImpl;
import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.model.bean.comptabilite.LigneEcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.SequenceEcritureComptable;
import com.dummy.myerp.technical.exception.FunctionalException;

@RunWith(MockitoJUnitRunner.class)
public class ComptabiliteManagerImplTest {

	private ComptabiliteManagerImpl manager = new ComptabiliteManagerImpl();
	private EcritureComptable vEcritureComptable = new EcritureComptable();
	private JournalComptable vJournal = new JournalComptable();
	
	/**
	 * Année pré-établie du journal comptable
	 */
	int EXPECTED_YEAR = 2010;
	
	/**
	 * Dernière valeur de la séquence pré-établie
	 */
	int DERNIERE_VALEUR = 120;
	private SequenceEcritureComptable sequence = new SequenceEcritureComptable(EXPECTED_YEAR,DERNIERE_VALEUR);
	
	/**
	 * Factorisation de toutes les opérations d'initialisation avec test
	 * 
	 */
	@Before
	public void Setup() {
		
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy"); 
		Date date=null;
		try {
		  date= df.parse("25-12-2010");
		} catch (ParseException e){
		  e.printStackTrace();
		}

		vJournal.setCode("AC");
		vJournal.setLibelle("Achat");
		vEcritureComptable.setJournal(vJournal);
		vEcritureComptable.setDate(date);
		vEcritureComptable.setLibelle("Achat outillage");
		EXPECTED_YEAR = 2010;
		DERNIERE_VALEUR = 120;
		
	}
	
	/*
	 * RG1:
	 * Le solde d'un compte comptable est égal à la somme des montants
	 *  au débit des lignes d'écriture diminuées de la somme des montants 
	 *  au crédit. Si le résultat est positif, le solde est dit "débiteur", 
	 *  si le résultat est négatif le solde est dit "créditeur".
	 *  
	 *  Situation normale: aucune exception attendue
	 * 
	 */

	@Test
	public void checkEcritureComptableUnit() throws Exception {

		String reference;
		Date date = vEcritureComptable.getDate();

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int annee = calendar.getWeekYear();
		reference = "AC-" + annee + "/" + "00001";
		vEcritureComptable.setReference(reference);
		
		vEcritureComptable.getListLigneEcriture()
				.add(new LigneEcritureComptable(new CompteComptable(1, "AC"), "opération1", new BigDecimal(123), null));
		vEcritureComptable.getListLigneEcriture()
				.add(new LigneEcritureComptable(new CompteComptable(2, "AC"), "opération1", null, new BigDecimal(123)));
				
		manager.checkEcritureComptableUnit(vEcritureComptable);
	}
	/*
	 * L'écriture comptable doit avoir une date
	 * Exception attendue: L'écriture comptable ne respecte pas les règles de gestion
	 * 
	 */
	
	@Test(expected = FunctionalException.class)
	public void checkEcritureComptableUnitEcritureSansDate() throws Exception {

		String reference;
		
		Date date = vEcritureComptable.getDate();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int annee = calendar.getWeekYear();
		
		reference = "AC-" + annee + "/" + "00001";
		vEcritureComptable.setReference(reference);
		
		vEcritureComptable.getListLigneEcriture()
				.add(new LigneEcritureComptable(new CompteComptable(1, "AC"), "opération1", new BigDecimal(123), null));
		vEcritureComptable.getListLigneEcriture()
				.add(new LigneEcritureComptable(new CompteComptable(2, "AC"), "opération1", null, new BigDecimal(123)));
		
		vEcritureComptable.setDate(null); // Ecriture comptable sans date
		manager.checkEcritureComptableUnit(vEcritureComptable);
	}
	
	/*
	 * Vérification des contraintes unitaires sur les attributs de l'écriture
	 * 
	 */

	@Test(expected = FunctionalException.class)
	public void checkEcritureComptableUnitViolation() throws Exception {

		manager.checkEcritureComptableUnit(vEcritureComptable);
	}
	
	/*
	 * L'écriture comptable doit être équilibrée
	 * Exception attendue: FunctionalException("L'écriture comptable n'est pas équilibrée.")
	 */

	@Test//(expected = FunctionalException.class)
	public void checkEcritureComptableUnitRG2() throws Exception {
		
		vEcritureComptable.getListLigneEcriture()
				.add(new LigneEcritureComptable(new CompteComptable(1), null, new BigDecimal(123), null));
		vEcritureComptable.getListLigneEcriture()
				.add(new LigneEcritureComptable(new CompteComptable(2), null, null, new BigDecimal(1234)));
				
		manager.checkEcritureComptableUnit(vEcritureComptable);
	}
	
	/*
	 * Pour qu'une écriture comptable soit valide, elle doit être équilibrée : 
	 * la somme des montants au crédit des lignes d'écriture doit être égale 
	 * à la somme des montants au débit.
	 * 
	 */

	@Test(expected = FunctionalException.class)
	public void checkEcritureComptableUnitRG2_equilibre() throws Exception {
		
		vEcritureComptable.getListLigneEcriture()
				.add(new LigneEcritureComptable(new CompteComptable(1), null, new BigDecimal(123), null));
		vEcritureComptable.getListLigneEcriture()
				.add(new LigneEcritureComptable(new CompteComptable(2), null, null, new BigDecimal(123)));
				
		manager.checkEcritureComptableUnit(vEcritureComptable);
	}
	
	/*
	 * Une écriture comptable doit contenir au moins deux lignes d'écriture :
	 *  une au débit et une au crédit.
	 *  Ici aucune erreur simulée
	 * 
	 */
	@Test//(expected = FunctionalException.class) aucune exception attendue!
	public void checkEcritureComptableUnitRG3SansErreur() throws Exception {
		
		vEcritureComptable.setReference("AC-2010/00121");
		vEcritureComptable.getListLigneEcriture()
				.add(new LigneEcritureComptable(new CompteComptable(1), null, new BigDecimal(123), null));
		vEcritureComptable.getListLigneEcriture()
				.add(new LigneEcritureComptable(new CompteComptable(1), null, null,  new BigDecimal(123)));
		manager.checkEcritureComptableUnit(vEcritureComptable);
	}
	
	/*
	 * Une écriture comptable doit contenir au moins deux lignes d'écriture :
	 *  une au débit et une au crédit.
	 *  Ici l'Ecrititure comptable n'a pas de référence
	 *  Exception attendue: FunctionalException("La référence de l'écriture comptable ne doit pas être nulle");

	 * 
	 */
	@Test(expected = FunctionalException.class) 
	public void checkEcritureComptableUnitRG3SansReference() throws Exception {
		
		//vEcritureComptable.setReference("AC-2010/00121");
		vEcritureComptable.getListLigneEcriture()
				.add(new LigneEcritureComptable(new CompteComptable(1), null, new BigDecimal(123), null));
		vEcritureComptable.getListLigneEcriture()
				.add(new LigneEcritureComptable(new CompteComptable(1), null, null,  new BigDecimal(123)));
		manager.checkEcritureComptableUnit(vEcritureComptable);
	}
	
	/*
	 * Une écriture comptable doit contenir au moins deux lignes d'écriture :
	 *  une au débit et une au crédit.
	 *  Ici deux lignes sont au crédit
	 *  L'écriture n'est pas équilibrée.
	 *  Exception attendue: FunctionalException("L'écriture comptable n'est pas équilibrée."
	 * 
	 */
	@Test(expected = FunctionalException.class)
	public void checkEcritureComptableUnitRG3() throws Exception {
		
		vEcritureComptable.setReference("AC-2010/00121");
		vEcritureComptable.getListLigneEcriture()
				.add(new LigneEcritureComptable(new CompteComptable(1), null, new BigDecimal(123), null));
		vEcritureComptable.getListLigneEcriture()
				.add(new LigneEcritureComptable(new CompteComptable(1), null, new BigDecimal(123), null));
		manager.checkEcritureComptableUnit(vEcritureComptable);
	}
	
	/*
	 * Une écriture comptable doit contenir au moins deux lignes d'écriture :
	 *  une au débit et une au crédit.
	 *  Ici une seule ligne
	 *  Exception attendue: "L'écriture comptable ne respecte pas les contraintes de validation"
	 * 
	 */
	@Test(expected = FunctionalException.class)
	public void checkEcritureComptableUnitRG3_uneLigne() throws Exception {

		vEcritureComptable.getListLigneEcriture()
				.add(new LigneEcritureComptable(new CompteComptable(1), null, new BigDecimal(123), null));
	//	vEcritureComptable.getListLigneEcriture()
	//		    .add(new LigneEcritureComptable(new CompteComptable(1), null, new BigDecimal(123), null));
		manager.checkEcritureComptableUnit(vEcritureComptable);
	}
	
	/*
	 * Test dans le cadre de la vérification de la méthode addReference
	 */
	
	@Test
	public void getYearOkTest() {
		
		int annee = manager.getYear(vEcritureComptable);
		assertEquals(EXPECTED_YEAR, annee);
	
	}
	
	/*
	 * Test dans le cadre de la vérification de la méthode addReference
	 */
	
	@Test
	public void referenceBuilderSetterTest() {
		
		String codeJournal = vJournal.getCode();
		String reference = manager.referenceBuilderSetter(EXPECTED_YEAR, sequence, codeJournal);
		int DERNIERE_VAL = sequence.getDerniereValeur();
		String EXPECTED_REF = codeJournal + "-" + String.valueOf(EXPECTED_YEAR) +  "/" + "00"  + String.valueOf(DERNIERE_VAL) ; //"AC-2010/00121";
		assertEquals(EXPECTED_REF, reference);
		
	}
	
	

}
