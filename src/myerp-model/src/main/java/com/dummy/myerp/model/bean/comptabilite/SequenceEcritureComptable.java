package com.dummy.myerp.model.bean.comptabilite;

/**
 * Bean représentant une séquence pour les références d'écriture comptable
 */
public class SequenceEcritureComptable {

	// ==================== Attributs ====================
	/** L'année */
	private Integer annee;
	/** La dernière valeur utilisée */
	private Integer derniereValeur;

	/** Le journal */
	private String journalCode; // version initiale
	// private JournalComptable journal;  // Autre alternative

	// ==================== Constructeurs ====================
	/**
	 * Constructeur
	 */
	public SequenceEcritureComptable() {
	}

	/**
	 * Constructeur
	 *
	 * @param pAnnee          -
	 * @param pDerniereValeur -
	 */
	public SequenceEcritureComptable(Integer pAnnee, Integer pDerniereValeur) {
		annee = pAnnee;
		derniereValeur = pDerniereValeur;
	}

	/*
	public SequenceEcritureComptable(Integer annee, Integer derniereValeur, JournalComptable journal) {
		super();
		this.annee = annee;
		this.derniereValeur = derniereValeur;
		this.journal = journal;
	}
	*/
	
	
	// ==================== Getters/Setters ====================
	public Integer getAnnee() {
		return annee;
	}

	public SequenceEcritureComptable(Integer annee, Integer derniereValeur, String journalCode) {
		super();
		this.annee = annee;
		this.derniereValeur = derniereValeur;
		this.journalCode = journalCode;
	}

	public void setAnnee(Integer pAnnee) {
		annee = pAnnee;
	}

	public Integer getDerniereValeur() {
		return derniereValeur;
	}

	public void setDerniereValeur(Integer pDerniereValeur) {
		derniereValeur = pDerniereValeur;
	}
	
	public String getJournalCode() {
		return journalCode;
	}

	public void setJournalCode(String journalCode) {
		this.journalCode = journalCode;
	}

	/*
	public JournalComptable getJournal() {
		return journal;
	}

	public void setJournal(JournalComptable journal) {
		this.journal = journal;
	}
	*/
	
	
	// ==================== Méthodes ====================
	@Override
	public String toString() {
		final StringBuilder vStB = new StringBuilder(this.getClass().getSimpleName());
		final String vSEP = ", ";
		vStB.append("{").append("Code journal=").append(journalCode).append(vSEP).append("annee=").append(annee).append(vSEP).append("derniereValeur=").append(derniereValeur)
				.append("}");
		return vStB.toString();
	}

	
}
