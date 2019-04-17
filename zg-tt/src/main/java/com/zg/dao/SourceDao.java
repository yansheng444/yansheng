package com.zg.dao;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.zg.base.BaseDao;
import com.zg.domain.Source;

@Repository
public class SourceDao extends BaseDao {
	
	
	public int create(Source s){
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("INSERT INTO source(name) VALUES (:name)");
		
		SqlParameterSource param = new BeanPropertySqlParameterSource(s);
		
		KeyHolder holder = new GeneratedKeyHolder();
		
		getAppJdbcTemplate().update(sql.toString(), param,holder);
		
		return holder.getKey().intValue();
	}
	
	
	public int batchCreate(List<Source> s){
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("INSERT INTO source(name) VALUES (:name)");
		
		SqlParameterSource[] createBatch = SqlParameterSourceUtils.createBatch(s.toArray());

		int[] batchUpdate = getAppJdbcTemplate().batchUpdate(sql.toString(), createBatch);
		
		for(int i :batchUpdate){
			if(i <= 0){
				return 0;
			}
		}
		
		return 1;
	}
	
	public List<Source> queryAll(){
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT * FROM source");
		
		return getJdbcTemplate().query(sql.toString(), new Object[]{},new BeanPropertyRowMapper<Source>(Source.class));
	}
	
}
