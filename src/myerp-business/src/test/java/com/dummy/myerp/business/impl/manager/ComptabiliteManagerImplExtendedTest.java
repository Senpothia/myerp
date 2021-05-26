package com.dummy.myerp.business.impl.manager;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.validation.constraints.AssertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.internal.runners.TestMethod;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import com.dummy.myerp.business.contrat.BusinessProxy;
import com.dummy.myerp.business.impl.TransactionManager;
import com.dummy.myerp.consumer.dao.contrat.DaoProxy;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.model.bean.comptabilite.SequenceEcritureComptable;

@RunWith(MockitoJUnitRunner.class)
//@ExtendWith(MockitoExtension.class)   
public class ComptabiliteManagerImplExtendedTest {

	@Mock
	BusinessProxy businessProxy;

	@Mock
	DaoProxy daoProxy;

	@Mock
	TransactionManager transactionManager;

	@Mock
	private ComptabiliteManagerImpl manager;

	private SequenceEcritureComptable sequence = new SequenceEcritureComptable();
	private JournalComptable journal = new JournalComptable();
	private EcritureComptable ecriture = new EcritureComptable();
	private int annee;

	@Before
	public void setUp() throws ParseException {

		MockitoAnnotations.initMocks(this);
		annee = 2014;
		journal.setCode("AC");
		journal.setLibelle("Fournisseur1");

		sequence.setAnnee(2014);
		sequence.setDerniereValeur(125);
		sequence.setJournalCode(journal.getCode());

		String sDate = "2014-05-17 00:00:00";
		Date date = new SimpleDateFormat("yyyy-mm-dd 00:00:00").parse(sDate);
		ecriture.setDate(date);
		ecriture.setJournal(journal);

		// manager = new ComptabiliteManagerImpl();

	}

	@Test
	public void invocationFindSequenceMethodTest() {

		//Mockito.when(manager.testMethode()).thenReturn(true);
		//boolean retour = manager.testCallback();
		
		//assertTrue(retour);
		// when(findSequence(journal, annee).check(3)).thenReturn(true);
		// manager.addReference(ecriture);
		// verify(manager, times(1)).findSequence(journal, annee);
		// manager.addReference(null);

	}

}
