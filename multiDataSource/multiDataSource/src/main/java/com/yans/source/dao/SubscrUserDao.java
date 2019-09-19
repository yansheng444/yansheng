package com.yans.source.dao;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.yans.source.base.BaseDao;
import com.yans.source.domain.SubscrUser;

@Repository
public class SubscrUserDao extends BaseDao {

	public int create(SubscrUser su){
		
		StringBuffer sql = new StringBuffer();
		
		sql.append(" INSERT INTO sub_user(trigger_id,user_id,process_id,status) VALUES(:trigger_id,:user_id,:process_id,:status)");
		
		SqlParameterSource param = new BeanPropertySqlParameterSource(su);
		return getAppJdbcTemplate().update(sql.toString(), param);
	}
	
	public int delete(SubscrUser su){
		
		StringBuffer sql = new StringBuffer();
		
		sql.append(" UPDATE sub_user SET is_delete = 1 ");
		sql.append("WHERE user_id = :user_id ");
		if(su.getTrigger_id() != null){
			sql.append(" AND trigger_id = :trigger_id ");
		}
		
		SqlParameterSource param = new BeanPropertySqlParameterSource(su);
		return getAppJdbcTemplate().update(sql.toString(), param);
	}
	
	public SubscrUser check(SubscrUser su){
		
		StringBuffer sql = new StringBuffer();
		
		sql.append(" SELECT * FROM sub_user ");
		sql.append("WHERE user_id = :user_id  AND is_delete = 0");
		
		if(su.getTrigger_id() != null){
			sql.append(" AND trigger_id = :trigger_id ");
		}
		
		SqlParameterSource param = new BeanPropertySqlParameterSource(su);
		
		BeanPropertyRowMapper<SubscrUser> mapper = new  BeanPropertyRowMapper<SubscrUser>(SubscrUser.class);
		
		List<SubscrUser> query = getAppJdbcTemplate().query(sql.toString(),param,mapper);
		
		if(query == null || query.size() == 0 ){
			return null; 
		}
		
		return query.get(0);
		
	}
	
	public int update(String status,String pid,String b_userid){
		
		StringBuffer sql = new StringBuffer();
		
		sql.append(" UPDATE sub_user SET status =  ");
		sql.append(status);
		if(!StringUtils.isEmpty(b_userid)){
			sql.append(" ,b_user_id = '").append(b_userid).append("'");
		}
		sql.append(" where process_id =  '");
		sql.append(pid).append("'");
		
		return getJdbcTemplate().update(sql.toString());
	}
		
		
	public int deleteFromPid(String pid){
	
		StringBuffer sql = new StringBuffer();
		
		sql.append(" update sub_user SET is_delete  = 1 ");
		sql.append(" WHERE process_id =  '");
		sql.append(pid).append("'");
		
		return getJdbcTemplate().update(sql.toString());
	}
	
	
	public List<SubscrUser> queryAll(Long pointid){
		
		StringBuffer sql = new StringBuffer();
		
		sql.append(" SELECT DISTINCT id id ,user_id user_id,b_user_id b_user_id,last_push_data last_push_data FROM sub_user ");
		sql.append("WHERE  is_delete = 0 AND status = 2 ");
		sql.append(" AND trigger_id = ");
		sql.append(pointid);
		
		List<SubscrUser> query = getJdbcTemplate().query(sql.toString(),new Object[]{},new BeanPropertyRowMapper<SubscrUser>(SubscrUser.class));
		
		if(query == null || query.size() == 0 ){
			return null; 
		}
		
		return query;
		
	}
	
	public SubscrUser queryOne(String pid){
		
		StringBuffer sql = new StringBuffer();
		
		sql.append(" SELECT * FROM sub_user ");
		sql.append("WHERE  is_delete = 0 ");
		sql.append(" AND process_id = '");
		sql.append(pid).append("'");
		
		List<SubscrUser> query = getJdbcTemplate().query(sql.toString(),new Object[]{},new BeanPropertyRowMapper<SubscrUser>(SubscrUser.class));
		
		if(query == null || query.size() == 0 ){
			return null; 
		}
		
		return query.get(0);
		
	}
	
	
	public int updatePushData(Long id ,String pushdata){
		
		StringBuffer sql = new StringBuffer();
		
		sql.append(" UPDATE sub_user SET last_push_data =  '");
		sql.append(pushdata).append("'");
		sql.append(" where id =  ");
		sql.append(id);
		return getJdbcTemplate().update(sql.toString());
	}
	
	
	
	
	
	
	
	
	
}
