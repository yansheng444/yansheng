package com.yans.source.dao;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.yans.source.base.BaseDao;
import com.yans.source.domain.PointTemplate;

@Repository
public class PointTemplateDao extends BaseDao {

	public PointTemplate queryOne(Long pointid) {

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT * FROM point_template WHERE trigger_id = ");
		sql.append(pointid);

		List<PointTemplate> query = getJdbcTemplate().query(sql.toString(), new Object[] {},
				new BeanPropertyRowMapper<PointTemplate>(PointTemplate.class));

		if (query == null || query.size() == 0) {
			return null;
		}

		return query.get(0);
	}

	public int update(PointTemplate p) {

		StringBuffer sql = new StringBuffer();

		sql.append(" UPDATE point_template update_time = NOW() ");
		if (p.getMessage() != null) {
			sql.append(" ,message = :message ");
		}
		sql.append(" WHERE trigger_id = :trigger_id ");

		SqlParameterSource param = new BeanPropertySqlParameterSource(p);
		return getAppJdbcTemplate().update(sql.toString(), param);
	}

	public int create(Long pointid, String template) {

		StringBuffer sql = new StringBuffer();

		sql.append(" INSERT INTO point_template(message,trigger_id) VALUES('");
		sql.append(template);
		sql.append("',");
		sql.append(pointid);
		sql.append(")");
		return getJdbcTemplate().update(sql.toString());
	}

}
