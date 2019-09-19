package com.yans.source.dao;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.yans.source.base.BaseDao;
import com.yans.source.domain.Group;

@Repository
public class GroupDao extends BaseDao {
	
	public int create(Group g){
		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO group(group_name) VALUES(:group_name)");
		SqlParameterSource param = new BeanPropertySqlParameterSource(g);
		return getAppJdbcTemplate().update(sql.toString(), param);
		
	}
	
	public List<Group> queryAll(){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM `group` where is_delete = 0 ");
		
		return getJdbcTemplate().query(sql.toString(), new Object[]{},new BeanPropertyRowMapper<Group>(Group.class));
	}
	
	public int delete(Long id ){
		StringBuffer sql = new StringBuffer();
		sql.append("update  group set is_delete = 1 where id = ");
		sql.append(id);
		
		return getJdbcTemplate().update(sql.toString());
	}
	
	public int update(Long id,String name ){
		StringBuffer sql = new StringBuffer();
		sql.append("update group set name = ");
		sql.append(name);
		sql.append(" where id = ");
		sql.append(id);
		
		return getJdbcTemplate().update(sql.toString());
	}
	
	
	
	
	
	
	
	
	
}
