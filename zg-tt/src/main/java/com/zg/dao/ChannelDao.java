package com.zg.dao;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.zg.base.BaseDao;
import com.zg.domain.Channel;

@Repository
public class ChannelDao extends BaseDao {
	
	
	public int create(Channel s){
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("INSERT INTO channel(name,sort) VALUES (:name,:sort)");
		
		SqlParameterSource param = new BeanPropertySqlParameterSource(s);
		
		KeyHolder holder = new GeneratedKeyHolder();
		
		getAppJdbcTemplate().update(sql.toString(), param,holder);
		
		return holder.getKey().intValue();
	}
	
	public List<Channel> queryAll(){
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM channel WHERE is_delete = 0 ORDER BY sort ASC ");
		return getJdbcTemplate().query(sql.toString(), new Object[]{},new BeanPropertyRowMapper<Channel>(Channel.class));
	}
	
	public int update(Channel c){
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("UPDATE channel SET update_time = NOW() ");
		
		if(!StringUtils.isEmpty(c.getName())){
			sql.append(",name = :name ");
		}
		if(c.getStatus() != null){
			sql.append(",status = :status ");
		}
		if(c.getSort() != null){
			sql.append(",sort = :sort ");
		}
		if(c.getIs_delete() != null){
			sql.append(",is_delete = :is_delete ");
		}
		
		sql.append(" WHERE id = :id ");
		
		SqlParameterSource param = new BeanPropertySqlParameterSource(c);
		
		return getAppJdbcTemplate().update(sql.toString(), param);
		
	}
	
	/**
	 * 更改排序
	 * @param start
	 * @param offset
	 * @return
	 */
	public int updateSort(int start ,int offset){
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" UPDATE channel SET sort = sort+");
		sql.append(offset);
		sql.append(" WHERE sort >");
		sql.append(start);
		sql.append("  AND status = 1");
		
		System.err.println(sql.toString());
		return getJdbcTemplate().update(sql.toString());
		
	}
	
	public int queryMaxSort(){
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT IFNULL(max(sort),0)  FROM channel"); 
		
		return getJdbcTemplate().queryForObject(sql.toString(), Integer.class);
	}
	
	/**
	 * 
	 * @param c
	 * @return
	 */
	public int batchUpdate(List<Channel> c){
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("UPDATE channel SET update_time = NOW() ");
		sql.append(",sort = :sort ");
		sql.append(" WHERE id = :id ");
		
		SqlParameterSource[] createBatch = SqlParameterSourceUtils.createBatch(c.toArray());

		int[] batchUpdate = getAppJdbcTemplate().batchUpdate(sql.toString(), createBatch);
		
		for(int i :batchUpdate){
			if(i <= 0){
				return 0;
			}
		}
		
		return 1;
		
	}

	public int queryByName(String name){
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM channel WHERE status = 1 WHERE is_delete = 0 AND name ='").append(name.trim()).append("'");
		List<Channel> query = getJdbcTemplate().query(sql.toString(), new Object[]{},new BeanPropertyRowMapper<Channel>(Channel.class));
		
		if(query == null || query.size() == 0 ){
			return 1;
		}
		return 0;
		
	}
	
	
	
	
	
	
	
}
