package com.dummy.myerp.business.impl.manager;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.TransactionStatus;
import com.dummy.myerp.business.contrat.manager.ComptabiliteManager;
import com.dummy.myerp.business.impl.AbstractBusinessManager;
import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.model.bean.comptabilite.LigneEcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.SequenceEcritureComptable;
import com.dummy.myerp.technical.exception.FunctionalException;
import com.dummy.myerp.technical.exception.NotFoundException;

/**
 * Comptabilite manager implementation.
 */
public class ComptabiliteManagerImpl extends AbstractBusinessManager implements ComptabiliteManager {

	// ==================== Attributs ====================

	// ==================== Constructeurs ====================
	/**
	 * Instantiates a new Comptabilite manager.
	 */
	public ComptabiliteManagerImpl() {
	}
	
	

	// ==================== Getters/Setters ====================
	@Override
	public List<CompteComptable> getListCompteComptable() {
		return getDaoProxy().getComptabiliteDao().getListCompteComptable();
	}

	@Override
	public List<JournalComptable> getListJournalComptable() {
		return getDaoProxy().getComptabiliteDao().getListJournalComptable();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<EcritureComptable> getListEcritureComptable() {
		return getDaoProxy().getComptabiliteDao().getListEcritureComptable();
	}

	/**
	 * {@inheritDoc}
	 */
	// TODO à tester (fait)
	@Override
	public synchronized void addReference(EcritureComptable pEcritureComptable) {
		// TODO à implémenter (fait)
		// Bien se réferer à la JavaDoc de cette méthode !
		/*
		 * Le principe :
		 * 1. Remonter depuis la persitance la dernière valeur de la
		 * séquence du journal pour l'année de l'écriture (table sequence_ecriture_comptable)
		 * 2. *
		 *  S'il n'y a aucun enregistrement pour le journal pour l'année concernée :
		 * 			1. Utiliser le numéro 1.
		 * 	Sinon : 2. Utiliser la dernière valeur + 1
		 * 3. Mettre à jour la référence de l'écriture avec la
		 * référence calculée (RG_Compta_5: règle de composition de la référence, ex: AC-2012/00012)
		 *  4. Enregistrer (insert/update) la valeur de
		 * la séquence en persitance (table sequence_ecriture_comptable)
		 */

		JournalComptable journal = pEcritureComptable.getJournal();
		int annee = getYear(pEcritureComptable);
		String journalCode = journal.getCode();
		SequenceEcritureComptable sequence = findSequence(journal, annee);
		int derniere_valeur;
		if (sequence != null){
			derniere_valeur = sequence.getDerniereValeur();
			derniere_valeur++;
			updateSequence(derniere_valeur, sequence);

		}else{

			sequence = new SequenceEcritureComptable();
			sequence.setJournalCode(journalCode);
			sequence.setAnnee(annee);
			derniere_valeur = 0;
			sequence.setDerniereValeur(derniere_valeur);
			insertSequence(sequence);
		}
		String reference = referenceBuilderSetter(annee, sequence, journalCode);
		pEcritureComptable.setReference(reference);

	}

	/**
	 * {@inheritDoc}
	 */

	@Override
	public void checkEcritureComptable(EcritureComptable pEcritureComptable) throws FunctionalException {
		this.checkEcritureComptableUnit(pEcritureComptable);
		this.checkEcritureComptableContext(pEcritureComptable);
	}

	/**
	 * Vérifie que l'Ecriture comptable respecte les règles de gestion unitaires,
	 * c'est à dire indépendemment du contexte (unicité de la référence, exerce
	 * comptable non cloturé...)
	 *
	 * @param pEcritureComptable -
	 * @throws FunctionalException Si l'Ecriture comptable ne respecte pas les
	 *                             règles de gestion
	 */
	// TODO tests à compléter
	protected void checkEcritureComptableUnit(EcritureComptable pEcritureComptable) throws FunctionalException {
		// ===== Vérification des contraintes unitaires sur les attributs de l'écriture
		Set<ConstraintViolation<EcritureComptable>> vViolations = getConstraintValidator().validate(pEcritureComptable);
		if (!vViolations.isEmpty()) {
			throw new FunctionalException("L'écriture comptable ne respecte pas les règles de gestion.",
					new ConstraintViolationException(
							"L'écriture comptable ne respecte pas les contraintes de validation", vViolations));
		}

		// ===== RG_Compta_2 : Pour qu'une écriture comptable soit valide, elle doit
		// être équilibrée
		if (!pEcritureComptable.isEquilibree()) {
			throw new FunctionalException("L'écriture comptable n'est pas équilibrée.");
		}

		// ===== RG_Compta_3 : une écriture comptable doit avoir au moins 2 lignes
		// d'écriture (1 au débit, 1 au crédit)

		int vNbrCredit = 0;
		int vNbrDebit = 0;
		for (LigneEcritureComptable vLigneEcritureComptable : pEcritureComptable.getListLigneEcriture()) {
			if (BigDecimal.ZERO
					.compareTo(ObjectUtils.defaultIfNull(vLigneEcritureComptable.getCredit(), BigDecimal.ZERO)) != 0) {
				vNbrCredit++;
			}
			if (BigDecimal.ZERO
					.compareTo(ObjectUtils.defaultIfNull(vLigneEcritureComptable.getDebit(), BigDecimal.ZERO)) != 0) {
				vNbrDebit++;
			}
		}
		// On test le nombre de lignes car si l'écriture à une seule ligne
		// avec un montant au débit et un montant au crédit ce n'est pas valable
		if (pEcritureComptable.getListLigneEcriture().size() < 2 || vNbrCredit < 1 || vNbrDebit < 1) {
			throw new FunctionalException(
					"L'écriture comptable doit avoir au moins deux lignes : une ligne au débit et une ligne au crédit.");
		}

		// TODO ===== RG_Compta_5 : Format et contenu de la référence
		// vérifier que l'année dans la référence correspond bien à la date de
		// l'écriture, idem pour le code journal...

		String reference = pEcritureComptable.getReference();

		if (reference == null) {

			throw new FunctionalException(

					"La référence de l'écriture comptable ne doit pas être nulle");

		}

		Date date = pEcritureComptable.getDate();

		if (date == null) {

			throw new FunctionalException(

					"La date de l'écriture comptable doit être définie");

		} else {

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			int annee = calendar.getWeekYear();

			try {

				String[] arrSplit1 = reference.split("-");

				try {

					String[] arrSplit2 = arrSplit1[1].split("/");

					if (!String.valueOf(annee).equals(arrSplit2[0])) {

						throw new FunctionalException(

								"La référence de l'écriture comptable doit comporter l'année. Exemple: XX-AAAA/#####.");
					}

				} catch (Exception e) {
					// handle exception
				}

				JournalComptable journal = pEcritureComptable.getJournal();
				String code_journal = journal.getCode();
				if (!arrSplit1[0].equals(code_journal)){
					throw new FunctionalException(

							"La référence de l'écriture comptable doit comporter le code du journal. Exemple: XX-AAAA/#####.");

				}

			} catch (Exception e) {
				// handle exception
			}



		}

	}

	/**
	 * Vérifie que l'Ecriture comptable respecte les règles de gestion liées au
	 * contexte (unicité de la référence, année comptable non cloturé...)
	 *
	 * @param pEcritureComptable -
	 * @throws FunctionalException Si l'Ecriture comptable ne respecte pas les
	 *                             règles de gestion
	 */
	protected void checkEcritureComptableContext(EcritureComptable pEcritureComptable) throws FunctionalException {
		// ===== RG_Compta_6 : La référence d'une écriture comptable doit être unique
		if (StringUtils.isNoneEmpty(pEcritureComptable.getReference())) {
			try {
				// Recherche d'une écriture ayant la même référence
				EcritureComptable vECRef = getDaoProxy().getComptabiliteDao()
						.getEcritureComptableByRef(pEcritureComptable.getReference());

				// Si l'écriture à vérifier est une nouvelle écriture (id == null),
				// ou si elle ne correspond pas à l'écriture trouvée (id != idECRef),
				// c'est qu'il y a déjà une autre écriture avec la même référence
				if (pEcritureComptable.getId() == null || !pEcritureComptable.getId().equals(vECRef.getId())) {
					throw new FunctionalException("Une autre écriture comptable existe déjà avec la même référence.");
				}
			} catch (NotFoundException vEx) {
				// Dans ce cas, c'est bon, ça veut dire qu'on n'a aucune autre écriture avec la
				// même référence.
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void insertEcritureComptable(EcritureComptable pEcritureComptable) throws FunctionalException {
		this.checkEcritureComptable(pEcritureComptable);
		TransactionStatus vTS = getTransactionManager().beginTransactionMyERP();
		try {
			getDaoProxy().getComptabiliteDao().insertEcritureComptable(pEcritureComptable);
			getTransactionManager().commitMyERP(vTS);
			vTS = null;
		} finally {
			getTransactionManager().rollbackMyERP(vTS);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateEcritureComptable(EcritureComptable pEcritureComptable) throws FunctionalException {
		TransactionStatus vTS = getTransactionManager().beginTransactionMyERP();
		try {
			getDaoProxy().getComptabiliteDao().updateEcritureComptable(pEcritureComptable);
			getTransactionManager().commitMyERP(vTS);
			vTS = null;
		} finally {
			getTransactionManager().rollbackMyERP(vTS);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteEcritureComptable(Integer pId) {
		TransactionStatus vTS = getTransactionManager().beginTransactionMyERP();
		try {
			getDaoProxy().getComptabiliteDao().deleteEcritureComptable(pId);
			getTransactionManager().commitMyERP(vTS);
			vTS = null;
		} finally {
			getTransactionManager().rollbackMyERP(vTS);
		}
	}
	
	public SequenceEcritureComptable findSequence(JournalComptable journal, int annee) {
		
		SequenceEcritureComptable sequence = getDaoProxy().getComptabiliteDao().getLastSequence(journal, annee);
		return sequence;
	}
	
	public void updateSequence(int derniere_valeur, SequenceEcritureComptable sequence) {
		
		getDaoProxy().getComptabiliteDao().updateSequenceEcritureComptable(sequence, derniere_valeur);
		
	}

	public void insertSequence(SequenceEcritureComptable sequence){

		getDaoProxy().getComptabiliteDao().insertSequenceEcritureComptable(sequence);
	}
	
	public int getYear(EcritureComptable pEcritureComptable) {
		
		Date date = pEcritureComptable.getDate();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int annee = calendar.getWeekYear();
		
		return annee;
	}
	
	public String referenceBuilderSetter(int annee, SequenceEcritureComptable sequence, String journalCode) {
		
		String reference;
		int derniere_valeur;

		if (sequence == null) {
			derniere_valeur = 1;
			sequence = new SequenceEcritureComptable(annee, derniere_valeur, journalCode);

		} else {

			derniere_valeur = sequence.getDerniereValeur() + 1;
			sequence.setDerniereValeur(derniere_valeur);

		}

		String numero = String.valueOf(derniere_valeur);
		int nombre = numero.length();

		while (nombre < 5) {

			numero = "0" + numero;
			nombre = numero.length();
		}

		reference = journalCode + "-" + annee + "/" + numero;
		return reference;
		
	}

	public int obtenirDerniereValeurSequence(String journal_code, int annee){

		return getDaoProxy().getComptabiliteDao().getDerniereValeurSequence(journal_code,annee);
	}
}
