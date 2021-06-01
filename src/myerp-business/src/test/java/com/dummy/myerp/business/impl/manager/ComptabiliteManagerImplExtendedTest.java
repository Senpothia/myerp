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
import com.dummy.myerp.consumer.dao.contrat.ComptabiliteDao;
import com.dummy.myerp.consumer.dao.contrat.DaoProxy;
import com.dummy.myerp.consumer.dao.impl.DaoProxyImpl;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.model.bean.comptabilite.SequenceEcritureComptable;


@RunWith(MockitoJUnitRunner.class)
public class ComptabiliteManagerImplExtendedTest {

    @Mock
     BusinessProxy businessProxy;


    DaoProxyImpl daoProxy;

    @Mock
    TransactionManager transactionManager;

    @Mock
    ComptabiliteDao comptabiliteDao;


    ComptabiliteManagerImpl manager;

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

        manager = new ComptabiliteManagerImpl();
        daoProxy = DaoProxyImpl.getInstance();
        daoProxy.setComptabiliteDao(comptabiliteDao);
        manager.configure(businessProxy, daoProxy, transactionManager);

    }

    @Test
    public void invocationFindSequenceMethodTest() {

        // Mockito.when(this.manager.findSequence(journal, annee)).thenReturn(sequence);
        //Mockito.when(this.manager.getDaoProxy()).thenReturn(daoProxy);
        //Mockito.when(daoProxy.getComptabiliteDao()).thenReturn(comptabiliteDao);
        Mockito.when(this.manager.getDaoProxy().getComptabiliteDao().getLastSequence(journal, annee)).thenReturn(sequence);
        // boolean retour = manager.testCallback();

        // assertTrue(retour);
        // when(findSequence(journal, annee).check(3)).thenReturn(true);
        // manager.addReference(ecriture);
        // verify(manager, times(1)).findSequence(journal, annee);
        // manager.addReference(null);

    }

}
