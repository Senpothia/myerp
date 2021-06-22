package com.dummy.myerp.business.impl.manager;


import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.validation.constraints.AssertTrue;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.internal.runners.TestMethod;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.*;
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
    @Captor
    ArgumentCaptor<SequenceEcritureComptable> captor;
    @Captor
    ArgumentCaptor<String> referenceCaptor;


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

    /**
     * Test des l'appels aux méthodes findSequence() et saveSequence()
     * getLasSequence() est appelée à l'interieur de la méthode findSequence avec deux arguments
     * La méthode est appelée sur le mock de comptabiliteDao
     * La methode insertSeqenceEcritureComptable est appelée sur le mock de comptabiliteDao
     * avec l'argument sequence retourne par When ThenReturn
     */
    @Test
    public void invocationDaoMethodsAddRenferenceTest() {

        Mockito.when(this.manager.getDaoProxy().getComptabiliteDao().getLastSequence(journal, annee)).thenReturn(sequence);
        manager.addReference(ecriture);
        Mockito.verify(comptabiliteDao).getLastSequence(journal,annee);
        Mockito.verify(comptabiliteDao).updateSequenceEcritureComptable(sequence, sequence.getDerniereValeur());
        Assert.assertEquals("AC-2014/00126", ecriture.getReference());

    }

    @Test
    public void invocationDaoMethodsAddRenferenceSequenceNullTest() {

        Mockito.when(this.manager.getDaoProxy().getComptabiliteDao().getLastSequence(journal, annee)).thenReturn(null);
        manager.addReference(ecriture);
        Mockito.verify(comptabiliteDao).getLastSequence(journal,annee);
        Mockito.verify(comptabiliteDao).insertSequenceEcritureComptable(anyObject());
        Mockito.verify(comptabiliteDao).insertSequenceEcritureComptable(captor.capture());
        boolean argumentMatcher = false;
        SequenceEcritureComptable sequenceArgument = captor.getValue();
        if (sequenceArgument.getDerniereValeur() == 1
        && sequenceArgument.getAnnee() == 2014
        && sequenceArgument.getJournalCode().equals("AC")){
            argumentMatcher = true;
        }
        Assert.assertTrue(argumentMatcher);

    }

}
