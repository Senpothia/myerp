import java.util.List;

import org.junit.Test;

import com.dummy.myerp.business.impl.manager.ComptabiliteManagerImpl;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:bootstrapContext.xml"})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ComptabiliteManagerImplTest extends BusinessTestCase {
	
	private ComptabiliteManagerImpl manager;
	
	@Test
	public void voirListeEcrituresComptable() {
		
	
		
		List<JournalComptable> journaux = manager.getListJournalComptable();
		System.out.println("Taille liste: " + journaux.size());
		
		//List<EcritureComptable> liste = manager.getListEcritureComptable();
		//EcritureComptable e = new EcritureComptable();
		
		//manager.configure(null, null, null);
		//System.out.println(manager);
	
		//List<CompteComptable> comptes = managerCompta.getListCompteComptable();
		
		
		
	}

}
