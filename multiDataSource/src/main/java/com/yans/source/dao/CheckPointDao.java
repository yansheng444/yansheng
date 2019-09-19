package com.yans.source.dao;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.yans.source.base.BaseDao;
import com.yans.source.base.Page;
import com.yans.source.domain.CheckPoint;
import com.yans.source.dto.CheckPointDto;
import com.yans.source.dto.CheckPointInfoDto;

@Repository
public class CheckPointDao extends BaseDao {
	
	public int create(CheckPoint c){
		
		StringBuffer sql = new StringBuffer();
		
		sql.append("INSERT INTO check_point(name,`condition`,cp_etl_batch,cp_collector,group_id,expire_time) ");
		sql.append("VALUES(:name,:condition,:cp_etl_batch,:cp_collector,:group_id,:expire_time)");
		
		SqlParameterSource param = new BeanPropertySqlParameterSource(c);
		
		KeyHolder holder =	new GeneratedKeyHolder();

		getAppJdbcTemplate().update(sql.toString(), param,holder);
		
		return holder.getKey().intValue();
		
	}
	
	public List<CheckPointDto> queryData(CheckPoint c ,Page page){
		
		StringBuffer sql  = new StringBuffer();
		
		sql.append(" SELECT *   ");
		sql.append("  FROM check_point p           ");
		sql.append(" WHERE  p.is_delete = 0                                   ");
		
		if(page != null){
			sql.append(" limit ").append(page.getPageSize() * (page.getCurPage()-1)).append(",").append(page.getPageSize());
		}
		
		SqlParameterSource param = new BeanPropertySqlParameterSource(c);
		BeanPropertyRowMapper<CheckPointDto> mapper = new BeanPropertyRowMapper<CheckPointDto>();
		
		List<CheckPointDto> query = getAppJdbcTemplate().query(sql.toString(), param,mapper);
		
		if(query == null || query.size() == 0 ){
			return null;
		}
		
		return query;
		
	}
	
	//我的消息通知订阅数据
	public List<CheckPointDto> queryOwenerData(String userid){
		
		StringBuffer sql  = new StringBuffer();
		
		sql.append(" SELECT td.id id ,td.name name  FROM trigger_detail td,sub_user su     ");
		sql.append(" WHERE td.id = su.trigger_id    ");
		sql.append("  AND td.is_delete = 0 AND su.is_delete = 0 AND su.`status` = 2              ");
		sql.append("  AND su.user_id =             ");
		sql.append(userid);
		
		List<CheckPointDto> query = getJdbcTemplate().query(sql.toString(), new Object[]{},new BeanPropertyRowMapper<CheckPointDto>(CheckPointDto.class));
		
		if(query == null || query.size() == 0 ){
			return null;
		}
		
		return query;
		
	}
	
	
	public List<CheckPoint> queryAll(){
		
		StringBuffer sql  = new StringBuffer();
		
		sql.append(" SELECT *   ");
		sql.append("  FROM check_point p           ");
		sql.append(" WHERE  p.is_delete = 0                                   ");
		
		List<CheckPoint> query = getJdbcTemplate().query(sql.toString(),new Object[]{},new BeanPropertyRowMapper<CheckPoint>(CheckPoint.class));
		
		if(query == null || query.size() == 0 ){
			return null;
		}
		
		return query;
		
	}

	public int delete(Long id ){
		
		StringBuffer sql  = new StringBuffer();
		
		sql.append(" UPDATE  check_point cp  ");
		sql.append(" SET cp.is_delete = 1 ");
		sql.append(" WHERE cp.id =     ");
		sql.append(id);
		
		return getJdbcTemplate().update(sql.toString());
		
	}

	public CheckPointInfoDto queryOne(String id) {
		
		StringBuffer sql  = new StringBuffer();
		
		sql.append("  SELECT *  ");  
		sql.append("  FROM check_point cp                                                       ");  
		sql.append("  WHERE                                         ");  
		sql.append("   cp.is_delete = 0 AND cp.id =                                                               ");  
		
//		sql.append(" SELECT name name,time_delay time_delay,sql_detail input FROM check_point WHERE is_delete = 0 AND id =   ");
		sql.append(id);
		
		 List<CheckPointInfoDto> query = getJdbcTemplate().query(sql.toString(),new Object[]{},new BeanPropertyRowMapper<CheckPointInfoDto>(CheckPointInfoDto.class));
		
		
		 if(query == null || query.size() == 0 ){
			 return null;
		 }
		 
		 return query.get(0);
	}
	
	
	public int update(CheckPoint c ){
		
		StringBuffer sql  = new StringBuffer();
		
		sql.append(" UPDATE check_point SET update_time = NOW()  ");
		
		if(!StringUtils.isEmpty(c.getCondition())){
			sql.append(" ,`condition` = :condition ");
		}
		if(c.getName() != null){
			sql.append(",name = :name ");
		}
		if(c.getCp_collector() != null){
			sql.append(",cp_collector = :cp_collector");
		}
		if(c.getCp_etl_batch() != null){
			sql.append(",cp_etl_batch = :cp_etl_batch");
		}
		if(c.getExpire_time() != null){
			sql.append(",expire_time = :expire_time ");
		}
		
		sql.append("where id = :id ");
		SqlParameterSource param = new BeanPropertySqlParameterSource(c);
		
		return getAppJdbcTemplate().update(sql.toString(),param);
		
	}
	
	
	
	public List<CheckPoint> queryList(){
		
		StringBuffer sql  = new StringBuffer();
		sql.append(" SELECT g.group_name group_name,cp.*,g.id group_id FROM `group` g LEFT JOIN check_point cp  ");
		sql.append(" ON g.id = cp.group_id AND g.is_delete = 0 and cp.is_delete = 0 ");
		
		List<CheckPoint> query = getJdbcTemplate().query(sql.toString(), new Object[]{},new BeanPropertyRowMapper<CheckPoint>(CheckPoint.class));
		
		if(query == null || query.size() == 0 ){
			return null;
		}
		
		
		return query;
		
		
	}
	
	
	
	
	
	
	
	
	
	
}
