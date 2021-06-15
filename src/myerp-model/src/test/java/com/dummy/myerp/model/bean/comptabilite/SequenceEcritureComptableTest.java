package com.dummy.myerp.model.bean.comptabilite;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SequenceEcritureComptableTest {

    private SequenceEcritureComptable sequence;

    @Before
    public void setup(){

        sequence = new SequenceEcritureComptable();
        sequence.setJournalCode("TT");
        sequence.setAnnee(2020);
        sequence.setDerniereValeur(100);
    }

    @Test
    public void getJournalEcritureComptableTest(){

        String code_journal = sequence.getJournalCode();
        Assert.assertEquals("TT", code_journal);

    }

    @Test
    public void getAnneeEcritureComptableTest(){

        int annee = sequence.getAnnee();
        Assert.assertEquals(2020, annee);
    }

    @Test
    public void getDerniereValeurEcritureComptableTest(){

        int derniere_valeur = sequence.getDerniereValeur();
        Assert.assertEquals(100, derniere_valeur);
    }

    @Test
    public void toStringEcritureComptableTest(){

        String printedSequence = sequence.toString();
        Assert.assertEquals("SequenceEcritureComptable{Code journal=TT, annee=2020, derniereValeur=100}", printedSequence);
    }
}
