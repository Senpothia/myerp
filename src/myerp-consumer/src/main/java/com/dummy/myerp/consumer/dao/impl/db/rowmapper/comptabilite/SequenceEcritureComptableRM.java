package com.dummy.myerp.consumer.dao.impl.db.rowmapper.comptabilite;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.dummy.myerp.consumer.dao.impl.cache.JournalComptableDaoCache;
import org.springframework.jdbc.core.RowMapper;

import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.SequenceEcritureComptable;

public class SequenceEcritureComptableRM implements RowMapper<SequenceEcritureComptable>{

	@Override
	public SequenceEcritureComptable mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		SequenceEcritureComptable vBean = new SequenceEcritureComptable();
		vBean.setAnnee(rs.getInt("annee"));
		vBean.setDerniereValeur(rs.getInt("derniere_valeur"));
		vBean.setJournalCode(rs.getString("journal_code"));
		return vBean;
	}

}
