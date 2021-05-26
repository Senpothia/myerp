package com.dummy.myerp.model.bean.comptabilite;

import java.math.BigDecimal;

import org.apache.commons.lang3.ObjectUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class EcritureComptableTest {

	private EcritureComptable vEcritureEquilibree;
	private EcritureComptable vEcritureNonEquilibree;

	@Before
	public void setUp() {  // ADD

		vEcritureEquilibree = new EcritureComptable();

		vEcritureEquilibree.setLibelle("Equilibrée");
		vEcritureEquilibree.getListLigneEcriture().add(this.createLigne(1, "200.50", null));
		vEcritureEquilibree.getListLigneEcriture().add(this.createLigne(1, "100.50", "33.00"));
		vEcritureEquilibree.getListLigneEcriture().add(this.createLigne(2, null, "301.00"));
		vEcritureEquilibree.getListLigneEcriture().add(this.createLigne(2, "40", "7.00"));

		vEcritureNonEquilibree = new EcritureComptable();

		vEcritureNonEquilibree.setLibelle("Non équilibrée");
		vEcritureNonEquilibree.getListLigneEcriture().add(this.createLigne(1, "10", null));
		vEcritureNonEquilibree.getListLigneEcriture().add(this.createLigne(1, "20", "1"));
		vEcritureNonEquilibree.getListLigneEcriture().add(this.createLigne(2, null, "30"));
		vEcritureNonEquilibree.getListLigneEcriture().add(this.createLigne(2, "1", "2"));

	}

	private LigneEcritureComptable createLigne(Integer pCompteComptableNumero, String pDebit, String pCredit) {
		BigDecimal vDebit = pDebit == null ? null : new BigDecimal(pDebit);
		BigDecimal vCredit = pCredit == null ? null : new BigDecimal(pCredit);
		String vLibelle = ObjectUtils.defaultIfNull(vDebit, BigDecimal.ZERO)
				.subtract(ObjectUtils.defaultIfNull(vCredit, BigDecimal.ZERO)).toPlainString();
		LigneEcritureComptable vRetour = new LigneEcritureComptable(new CompteComptable(pCompteComptableNumero),
				vLibelle, vDebit, vCredit);
		return vRetour;
	}

	@Test
	public void isEquilibreeTrue() {

		Assert.assertTrue(vEcritureEquilibree.toString(), vEcritureEquilibree.isEquilibree());

	}

	@Test
	public void isEquilibreeFalse() {

		Assert.assertFalse(vEcritureNonEquilibree.toString(), vEcritureNonEquilibree.isEquilibree());

	}

	@Test
	public void getTotalCreditTest() {

		BigDecimal totalCredit = vEcritureEquilibree.getTotalCredit();
		BigDecimal valReference = new BigDecimal("341.00");
		Assert.assertTrue("ok", totalCredit.equals(valReference));

	}

	@Test
	public void getTotalDebitTest() {

		BigDecimal totalDebit = vEcritureNonEquilibree.getTotalDebit();
		BigDecimal valReference = new BigDecimal("31");
		Assert.assertTrue("ok", totalDebit.equals(valReference));

	}

}
