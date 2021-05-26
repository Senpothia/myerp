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
	
	int EXPECTED_YEAR = 2010;
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
		int EXPECTED_YEAR = 2010;
		
	}

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

	@Test(expected = FunctionalException.class)
	public void checkEcritureComptableUnitViolation() throws Exception {

		manager.checkEcritureComptableUnit(vEcritureComptable);
	}

	@Test(expected = FunctionalException.class)
	public void checkEcritureComptableUnitRG2() throws Exception {
		
		vEcritureComptable.getListLigneEcriture()
				.add(new LigneEcritureComptable(new CompteComptable(1), null, new BigDecimal(123), null));
		vEcritureComptable.getListLigneEcriture()
				.add(new LigneEcritureComptable(new CompteComptable(2), null, null, new BigDecimal(1234)));
				
		manager.checkEcritureComptableUnit(vEcritureComptable);
	}

	@Test(expected = FunctionalException.class)
	public void checkEcritureComptableUnitRG3() throws Exception {

		vEcritureComptable.getListLigneEcriture()
				.add(new LigneEcritureComptable(new CompteComptable(1), null, new BigDecimal(123), null));
		vEcritureComptable.getListLigneEcriture()
				.add(new LigneEcritureComptable(new CompteComptable(1), null, new BigDecimal(123), null));
		manager.checkEcritureComptableUnit(vEcritureComptable);
	}
	
	@Test
	public void getYearOkTest() {
		
		int annee = manager.getYear(vEcritureComptable);
		assertEquals(EXPECTED_YEAR, annee);
	
	}
	
	@Test
	public void referenceBuilderSetterTest() {
		
		String codeJournal = vJournal.getCode();
		String reference = manager.referenceBuilderSetter(EXPECTED_YEAR, sequence, codeJournal);
		int DERNIERE_VAL = sequence.getDerniereValeur();
		String EXPECTED_REF = codeJournal + "-" + String.valueOf(EXPECTED_YEAR) +  "/" + "00"  + String.valueOf(DERNIERE_VAL) ; //"AC-2010/00121";
		assertEquals(EXPECTED_REF, reference);
		
	}
	
	

}
