package com.dummy.myerp.consumer.dao.impl.db.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import com.dummy.myerp.consumer.dao.contrat.ComptabiliteDao;
import com.dummy.myerp.consumer.dao.impl.db.rowmapper.comptabilite.CompteComptableRM;
import com.dummy.myerp.consumer.dao.impl.db.rowmapper.comptabilite.EcritureComptableRM;
import com.dummy.myerp.consumer.dao.impl.db.rowmapper.comptabilite.JournalComptableRM;
import com.dummy.myerp.consumer.dao.impl.db.rowmapper.comptabilite.LigneEcritureComptableRM;
import com.dummy.myerp.consumer.dao.impl.db.rowmapper.comptabilite.SequenceEcritureComptableRM;
import com.dummy.myerp.consumer.db.AbstractDbConsumer;
import com.dummy.myerp.consumer.db.DataSourcesEnum;
import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.model.bean.comptabilite.LigneEcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.SequenceEcritureComptable;
import com.dummy.myerp.technical.exception.NotFoundException;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

/**
 * Implémentation de l'interface {@link ComptabiliteDao}
 */
public class ComptabiliteDaoImpl extends AbstractDbConsumer implements ComptabiliteDao {

	// ==================== Constructeurs ====================
	/**
	 * Instance unique de la classe (design pattern Singleton)
	 */
	private static final ComptabiliteDaoImpl INSTANCE = new ComptabiliteDaoImpl();

	/**
	 * Renvoie l'instance unique de la classe (design pattern Singleton).
	 *
	 * @return {@link ComptabiliteDaoImpl}
	 */
	public static ComptabiliteDaoImpl getInstance() {
		return ComptabiliteDaoImpl.INSTANCE;
	}

	/**
	 * Constructeur.
	 */
	protected ComptabiliteDaoImpl() {
		super();
	}

	// ==================== Méthodes ====================
	/**
	 * SQLgetListCompteComptable
	 */
	private static String SQLgetListCompteComptable;

	public void setSQLgetListCompteComptable(String pSQLgetListCompteComptable) {
		SQLgetListCompteComptable = pSQLgetListCompteComptable;
	}

	@Override
	public List<CompteComptable> getListCompteComptable() {
		JdbcTemplate vJdbcTemplate = new JdbcTemplate(this.getDataSource(DataSourcesEnum.MYERP));
		CompteComptableRM vRM = new CompteComptableRM();
		List<CompteComptable> vList = vJdbcTemplate.query(SQLgetListCompteComptable, vRM);
		return vList;
	}

	/**
	 * SQLgetListJournalComptable
	 */
	private static String SQLgetListJournalComptable;

	public void setSQLgetListJournalComptable(String pSQLgetListJournalComptable) {
		SQLgetListJournalComptable = pSQLgetListJournalComptable;
	}

	@Override
	public List<JournalComptable> getListJournalComptable() {
		JdbcTemplate vJdbcTemplate = new JdbcTemplate(this.getDataSource(DataSourcesEnum.MYERP));
		JournalComptableRM vRM = new JournalComptableRM();
		List<JournalComptable> vList = vJdbcTemplate.query(SQLgetListJournalComptable, vRM);
		return vList;
	}

	// ==================== EcritureComptable - GET ====================

	/**
	 * SQLgetListEcritureComptable
	 */
	private static String SQLgetListEcritureComptable;

	public void setSQLgetListEcritureComptable(String pSQLgetListEcritureComptable) {
		SQLgetListEcritureComptable = pSQLgetListEcritureComptable;
	}

	@Override
	public List<EcritureComptable> getListEcritureComptable() {
		JdbcTemplate vJdbcTemplate = new JdbcTemplate(this.getDataSource(DataSourcesEnum.MYERP));
		EcritureComptableRM vRM = new EcritureComptableRM();
		List<EcritureComptable> vList = vJdbcTemplate.query(SQLgetListEcritureComptable, vRM);
		return vList;
	}

	/**
	 * SQLgetEcritureComptable
	 */
	private static String SQLgetEcritureComptable;

	public void setSQLgetEcritureComptable(String pSQLgetEcritureComptable) {
		SQLgetEcritureComptable = pSQLgetEcritureComptable;
	}

	@Override
	public EcritureComptable getEcritureComptable(Integer pId) throws NotFoundException {
		NamedParameterJdbcTemplate vJdbcTemplate = new NamedParameterJdbcTemplate(getDataSource(DataSourcesEnum.MYERP));
		MapSqlParameterSource vSqlParams = new MapSqlParameterSource();
		vSqlParams.addValue("id", pId);
		EcritureComptableRM vRM = new EcritureComptableRM();
		EcritureComptable vBean;
		try {
			vBean = vJdbcTemplate.queryForObject(SQLgetEcritureComptable, vSqlParams, vRM);
		} catch (EmptyResultDataAccessException vEx) {
			throw new NotFoundException("EcritureComptable non trouvée : id=" + pId);
		}
		return vBean;
	}

	/**
	 * SQLgetEcritureComptableByRef
	 */
	private static String SQLgetEcritureComptableByRef;

	public void setSQLgetEcritureComptableByRef(String pSQLgetEcritureComptableByRef) {
		SQLgetEcritureComptableByRef = pSQLgetEcritureComptableByRef;
	}

	@Override
	public EcritureComptable getEcritureComptableByRef(String pReference) throws NotFoundException {
		NamedParameterJdbcTemplate vJdbcTemplate = new NamedParameterJdbcTemplate(getDataSource(DataSourcesEnum.MYERP));
		MapSqlParameterSource vSqlParams = new MapSqlParameterSource();
		vSqlParams.addValue("reference", pReference);
		EcritureComptableRM vRM = new EcritureComptableRM();
		EcritureComptable vBean;
		try {
			vBean = vJdbcTemplate.queryForObject(SQLgetEcritureComptableByRef, vSqlParams, vRM);
		} catch (EmptyResultDataAccessException vEx) {
			throw new NotFoundException("EcritureComptable non trouvée : reference=" + pReference);
		}
		return vBean;
	}

	/**
	 * SQLloadListLigneEcriture
	 */
	private static String SQLloadListLigneEcriture;

	public void setSQLloadListLigneEcriture(String pSQLloadListLigneEcriture) {
		SQLloadListLigneEcriture = pSQLloadListLigneEcriture;
	}

	@Override
	public void loadListLigneEcriture(EcritureComptable pEcritureComptable) {
		NamedParameterJdbcTemplate vJdbcTemplate = new NamedParameterJdbcTemplate(getDataSource(DataSourcesEnum.MYERP));
		MapSqlParameterSource vSqlParams = new MapSqlParameterSource();
		vSqlParams.addValue("ecriture_id", pEcritureComptable.getId());
		LigneEcritureComptableRM vRM = new LigneEcritureComptableRM();
		List<LigneEcritureComptable> vList = vJdbcTemplate.query(SQLloadListLigneEcriture, vSqlParams, vRM);
		pEcritureComptable.getListLigneEcriture().clear();
		pEcritureComptable.getListLigneEcriture().addAll(vList);
	}

	// ==================== EcritureComptable - INSERT ====================

	/**
	 * SQLinsertEcritureComptable
	 */
	private static String SQLinsertEcritureComptable;

	public void setSQLinsertEcritureComptable(String pSQLinsertEcritureComptable) {
		SQLinsertEcritureComptable = pSQLinsertEcritureComptable;
	}

	@Override
	public void insertEcritureComptable(EcritureComptable pEcritureComptable) {
		// ===== Ecriture Comptable
		NamedParameterJdbcTemplate vJdbcTemplate = new NamedParameterJdbcTemplate(getDataSource(DataSourcesEnum.MYERP));
		MapSqlParameterSource vSqlParams = new MapSqlParameterSource();
		vSqlParams.addValue("journal_code", pEcritureComptable.getJournal().getCode());
		vSqlParams.addValue("reference", pEcritureComptable.getReference());
		vSqlParams.addValue("date", pEcritureComptable.getDate(), Types.DATE);
		vSqlParams.addValue("libelle", pEcritureComptable.getLibelle());

		vJdbcTemplate.update(SQLinsertEcritureComptable, vSqlParams);

		// ----- Récupération de l'id
		Integer vId = this.queryGetSequenceValuePostgreSQL(DataSourcesEnum.MYERP, "myerp.ecriture_comptable_id_seq",
				Integer.class);
		pEcritureComptable.setId(vId);

		// ===== Liste des lignes d'écriture
		this.insertListLigneEcritureComptable(pEcritureComptable);
	}

	/**
	 * SQLinsertListLigneEcritureComptable
	 */
	private static String SQLinsertListLigneEcritureComptable;

	public void setSQLinsertListLigneEcritureComptable(String pSQLinsertListLigneEcritureComptable) {
		SQLinsertListLigneEcritureComptable = pSQLinsertListLigneEcritureComptable;
	}

	/**
	 * Insert les lignes d'écriture de l'écriture comptable
	 *
	 * @param pEcritureComptable l'écriture comptable
	 */
	protected void insertListLigneEcritureComptable(EcritureComptable pEcritureComptable) {
		NamedParameterJdbcTemplate vJdbcTemplate = new NamedParameterJdbcTemplate(getDataSource(DataSourcesEnum.MYERP));
		MapSqlParameterSource vSqlParams = new MapSqlParameterSource();
		vSqlParams.addValue("ecriture_id", pEcritureComptable.getId());

		int vLigneId = 0;
		for (LigneEcritureComptable vLigne : pEcritureComptable.getListLigneEcriture()) {
			vLigneId++;
			vSqlParams.addValue("ligne_id", vLigneId);
			vSqlParams.addValue("compte_comptable_numero", vLigne.getCompteComptable().getNumero());
			vSqlParams.addValue("libelle", vLigne.getLibelle());
			vSqlParams.addValue("debit", vLigne.getDebit());

			vSqlParams.addValue("credit", vLigne.getCredit());

			vJdbcTemplate.update(SQLinsertListLigneEcritureComptable, vSqlParams);
		}
	}

	// ==================== EcritureComptable - UPDATE ====================

	/**
	 * SQLupdateEcritureComptable
	 */
	private static String SQLupdateEcritureComptable;

	public void setSQLupdateEcritureComptable(String pSQLupdateEcritureComptable) {
		SQLupdateEcritureComptable = pSQLupdateEcritureComptable;
	}

	@Override
	public void updateEcritureComptable(EcritureComptable pEcritureComptable) {
		// ===== Ecriture Comptable
		NamedParameterJdbcTemplate vJdbcTemplate = new NamedParameterJdbcTemplate(getDataSource(DataSourcesEnum.MYERP));
		MapSqlParameterSource vSqlParams = new MapSqlParameterSource();
		vSqlParams.addValue("id", pEcritureComptable.getId());
		vSqlParams.addValue("journal_code", pEcritureComptable.getJournal().getCode());
		vSqlParams.addValue("reference", pEcritureComptable.getReference());
		vSqlParams.addValue("date", pEcritureComptable.getDate(), Types.DATE);
		vSqlParams.addValue("libelle", pEcritureComptable.getLibelle());

		vJdbcTemplate.update(SQLupdateEcritureComptable, vSqlParams);

		// ===== Liste des lignes d'écriture
		this.deleteListLigneEcritureComptable(pEcritureComptable.getId());
		this.insertListLigneEcritureComptable(pEcritureComptable);
	}

	// ==================== EcritureComptable - DELETE ====================

	/**
	 * SQLdeleteEcritureComptable
	 */
	private static String SQLdeleteEcritureComptable;

	public void setSQLdeleteEcritureComptable(String pSQLdeleteEcritureComptable) {
		SQLdeleteEcritureComptable = pSQLdeleteEcritureComptable;
	}

	@Override
	public void deleteEcritureComptable(Integer pId) {
		// ===== Suppression des lignes d'écriture
		this.deleteListLigneEcritureComptable(pId);

		// ===== Suppression de l'écriture
		NamedParameterJdbcTemplate vJdbcTemplate = new NamedParameterJdbcTemplate(getDataSource(DataSourcesEnum.MYERP));
		MapSqlParameterSource vSqlParams = new MapSqlParameterSource();
		vSqlParams.addValue("id", pId);
		vJdbcTemplate.update(SQLdeleteEcritureComptable, vSqlParams);
	}

	/**
	 * SQLdeleteListLigneEcritureComptable
	 */
	private static String SQLdeleteListLigneEcritureComptable;

	public void setSQLdeleteListLigneEcritureComptable(String pSQLdeleteListLigneEcritureComptable) {
		SQLdeleteListLigneEcritureComptable = pSQLdeleteListLigneEcritureComptable;
	}

	/**
	 * Supprime les lignes d'écriture de l'écriture comptable d'id
	 * {@code pEcritureId}
	 *
	 * @param pEcritureId id de l'écriture comptable
	 */
	protected void deleteListLigneEcritureComptable(Integer pEcritureId) {
		NamedParameterJdbcTemplate vJdbcTemplate = new NamedParameterJdbcTemplate(getDataSource(DataSourcesEnum.MYERP));
		MapSqlParameterSource vSqlParams = new MapSqlParameterSource();
		vSqlParams.addValue("ecriture_id", pEcritureId);
		vJdbcTemplate.update(SQLdeleteListLigneEcritureComptable, vSqlParams);
	}

	/**
	 * SQLgetLastSequenceValue
	 */
	/*
	private static String SQLgetLastSequenceValue;

	public static void setSQLgetLastSequenceValue(String SQLgetLastSequenceValue) {
		ComptabiliteDaoImpl.SQLgetLastSequenceValue = SQLgetLastSequenceValue;
	}
	*/

	private static String SQLgetSequenceEcritureComptable;

	public static void setSQLgetSequenceEcritureComptable(String SQLgetSequenceEcritureComptable) {
		ComptabiliteDaoImpl.SQLgetSequenceEcritureComptable = SQLgetSequenceEcritureComptable;
	}

	@Override
	public SequenceEcritureComptable getLastSequence(JournalComptable journal, int annee) {

		String journal_code = journal.getCode();
		NamedParameterJdbcTemplate vJdbcTemplate = new NamedParameterJdbcTemplate(getDataSource(DataSourcesEnum.MYERP));
		MapSqlParameterSource vSqlParams = new MapSqlParameterSource();
		vSqlParams.addValue("journal_code", journal_code);
		vSqlParams.addValue("annee", annee);
		SequenceEcritureComptableRM vRM = new SequenceEcritureComptableRM();
		SequenceEcritureComptable vBean;
		try {
			vBean = vJdbcTemplate.queryForObject(SQLgetSequenceEcritureComptable, vSqlParams, vRM);
		} catch (EmptyResultDataAccessException vException) {
			return null;
		}
		return vBean;

	}


	/**
	 * SQLinsertSequenceComptable
	 */
	private static String SQLinsertSequenceComptable;

	public static void setSQLinsertSequenceComptable(String SQLinsertSequenceComptable) {
		ComptabiliteDaoImpl.SQLinsertSequenceComptable = SQLinsertSequenceComptable;
	}

	@Override
	public void insertSequenceEcritureComptable(SequenceEcritureComptable sequence) {

		NamedParameterJdbcTemplate vJdbcTemplate = new NamedParameterJdbcTemplate(getDataSource(DataSourcesEnum.MYERP));
		MapSqlParameterSource vSqlParams = new MapSqlParameterSource();
		vSqlParams.addValue("journal_code", sequence.getJournalCode());
		vSqlParams.addValue("annee", sequence.getAnnee());
		vSqlParams.addValue("derniere_valeur", sequence.getDerniereValeur());
		vJdbcTemplate.update(SQLinsertSequenceComptable, vSqlParams);

	}

	/**
	 * SQLupdateSequenceComptable
	 */

	private static String SQLupdateSequenceComptable;

	public static void setSQLupdateSequenceComptable(String SQLupdateSequenceComptable) {
		ComptabiliteDaoImpl.SQLupdateSequenceComptable = SQLupdateSequenceComptable;
	}

	@Override
	public void updateSequenceEcritureComptable(SequenceEcritureComptable sequence, int derniere_valeur) {

		NamedParameterJdbcTemplate vJdbcTemplate = new NamedParameterJdbcTemplate(getDataSource(DataSourcesEnum.MYERP));
		MapSqlParameterSource vSqlParams = new MapSqlParameterSource();
		vSqlParams.addValue("derniere_valeur", derniere_valeur);
		vSqlParams.addValue("journal_code", sequence.getJournalCode());
		vSqlParams.addValue("annee", sequence.getAnnee());

		vJdbcTemplate.update(SQLupdateSequenceComptable, vSqlParams);
	}


	/**
	 * SQLgetLastSequenceValue
	 */
	/*
	private static String SQLgetLastSequenceValue;

	public static void setSQLgetLastSequenceValue(String SQLgetLastSequenceValue) {
		ComptabiliteDaoImpl.SQLgetLastSequenceValue = SQLgetLastSequenceValue;
	}
	*/

	private String SQLgetLastSequenceValue;

	public void setSQLgetLastSequenceValue(String SQLgetLastSequenceValue) {
		this.SQLgetLastSequenceValue = SQLgetLastSequenceValue;
	}

	/**
	 * Retourne la dernière valeur d'une sequence à partir d'un code journal et une année
	 * donnés en paramètres
	 * @param journal_code
	 * @param annee
	 * @return
	 */
	@Override
	public int getDerniereValeurSequence(String journal_code, int annee) {

		//TODO (perso) implémenter méthode pour retourner directement la dernière valeur d'une sequence
		NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(getDataSource(DataSourcesEnum.MYERP));
		SqlParameterSource namedParameters = new MapSqlParameterSource()
				.addValue("journal_code", journal_code)
				.addValue("annee", annee);

		return namedParameterJdbcTemplate.queryForObject(
				SQLgetLastSequenceValue, namedParameters, Integer.class);
	}
	}
