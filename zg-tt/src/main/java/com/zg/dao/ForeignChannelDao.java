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
import com.zg.domain.ForeignChannel;

@Repository
public class ForeignChannelDao extends BaseDao {
	
	public int create(ForeignChannel s){
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("INSERT INTO foreign_channel(name) VALUES (:name)");
		
		SqlParameterSource param = new BeanPropertySqlParameterSource(s);
		
		KeyHolder holder = new GeneratedKeyHolder();
		
		getAppJdbcTemplate().update(sql.toString(), param,holder);
		
		return holder.getKey().intValue();
	}
	
	
	public int batchCreate(List<ForeignChannel> s){
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("INSERT INTO foreign_channel(name) VALUES (:name)");
		
		SqlParameterSource[] createBatch = SqlParameterSourceUtils.createBatch(s.toArray());

		int[] batchUpdate = getAppJdbcTemplate().batchUpdate(sql.toString(), createBatch);
		
		for(int i :batchUpdate){
			if(i <= 0){
				return 0;
			}
		}
		
		return 1;
	}
	
	public List<ForeignChannel> queryAll(){
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT * FROM foreign_channel");
		
		return getJdbcTemplate().query(sql.toString(), new Object[]{},new BeanPropertyRowMapper<ForeignChannel>(ForeignChannel.class));
	}
	
}
