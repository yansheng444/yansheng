package com.zg.dao;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Repository;

import com.zg.base.BaseDao;
import com.zg.domain.ContentConfig;
import com.zg.domain.Source;

@Repository
public class ContentConfigDao extends BaseDao {
	
	
	
	public int batchCreate(List<ContentConfig> s){
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("INSERT INTO content_cnf(source_name,channel_name,percent,channel_id,type) ");
		sql.append("VALUES(:source_name,:channel_name,:percent,:channel_id,:type)");
		
		SqlParameterSource[] createBatch = SqlParameterSourceUtils.createBatch(s.toArray());

		int[] batchUpdate = getAppJdbcTemplate().batchUpdate(sql.toString(), createBatch);
		
		for(int i :batchUpdate){
			if(i <= 0){
				return 0;
			}
		}
		
		return 1;
	}
	
	public List<Source> queryFromChannel(String channelId){
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT * FROM content_cnf WHERE is_delete = 0 AND channel_id = ");
		sql.append(channelId);
		
		return getJdbcTemplate().query(sql.toString(), new Object[]{},new BeanPropertyRowMapper<Source>(Source.class));
	}
	
}
