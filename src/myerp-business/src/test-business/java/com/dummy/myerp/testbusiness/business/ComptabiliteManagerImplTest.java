package com.dummy.myerp.testbusiness.business;
import java.util.List;

import org.junit.Assert;
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
	
	@Test
	public void voirListeEcrituresComptable() {

		manager = new ComptabiliteManagerImpl();
		List<JournalComptable> journaux = manager.getListJournalComptable();
		System.out.println("Taille liste: " + journaux.size());
		Assert.assertEquals(journaux.size(), 4);
		
		//List<EcritureComptable> liste = manager.getListEcritureComptable();
		//EcritureComptable e = new EcritureComptable();
		
		//manager.configure(null, null, null);
		//System.out.println(manager);
	
		//List<CompteComptable> comptes = managerCompta.getListCompteComptable();
		
		
		
	}

}
