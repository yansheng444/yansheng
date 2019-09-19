package com.yans.source.dao;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.yans.source.base.BaseDao;
import com.yans.source.domain.Trigger;

@Repository
public class TriggerDao extends BaseDao {

	public List<Trigger> queryData(String checkid,Integer is_follow,String search,String userid) {
		
		StringBuffer sql = new StringBuffer();
		
		sql.append(" 	select td.*                                                                ");
		if(!StringUtils.isEmpty(userid)){
			sql.append("  ,IFNULL(su.`status`,0) is_sub ");
		}
		sql.append(" 	 from trigger_detail td JOIN check_point cp                                                               ");
		sql.append(" 	ON td.check_id = cp.id AND cp.is_delete = 0  AND td.is_delete = 0 ");
		if(is_follow != null){
			sql.append("	AND td.tg_follow = ").append(is_follow);
		}
		if(!StringUtils.isEmpty(checkid)){
			sql.append(" AND td.check_id = ").append(checkid);
		}
		if(!StringUtils.isEmpty(search)){
			sql.append(" AND td.`name` LIKE '%").append(search).append("%'  ");
		}
		if(!StringUtils.isEmpty(userid)){
			sql.append("   LEFT JOIN sub_user su  ");
			sql.append(" ON td.id = su.trigger_id  AND su.is_delete = 0    ");
			sql.append(" AND su.user_id = ").append(userid);
		}
			
			return getJdbcTemplate().query(sql.toString(), new Object[] {},
					new BeanPropertyRowMapper<Trigger>(Trigger.class));
		}

	public List<Trigger> queryAll() {

		StringBuffer sql = new StringBuffer();
		sql.append(" select td.*   ");
		sql.append(" from trigger_detail td,check_point cp     ");
		sql.append(" WHERE td.check_id = cp.id                           ");
		sql.append(" AND cp.is_delete = 0 AND td.tg_follow = 0  AND td.is_delete = 0           ");
		return getJdbcTemplate().query(sql.toString(), new Object[] {},
				new BeanPropertyRowMapper<Trigger>(Trigger.class));
	}

	public int delete(Long id) {

		StringBuffer sql = new StringBuffer();
		sql.append("update trigger_detail set is_delete = 1 where id = ");
		sql.append(id);

		return getJdbcTemplate().update(sql.toString());
	}

	
	public Trigger queryOne(Long id,String userid) {

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT td.* ");
		if(!StringUtils.isEmpty(userid)){
			sql.append(",IFNULL((SELECT su.`status` from sub_user su where td.id = su.trigger_id AND su.is_delete = 0 AND su.user_id = ");
			sql.append(userid);
			sql.append("),0)  is_sub ");
		}
		sql.append(" FROM trigger_detail td ");

		sql.append(" WHERE td.id =  ");

		sql.append(id);

		List<Trigger> query = getJdbcTemplate().query(sql.toString(),new Object[]{},new BeanPropertyRowMapper<Trigger>(Trigger.class));
		
		if(query == null || query.size() == 0 ){
			return null;
		}
		
		return query.get(0);
	}
	
	public Long create(Trigger t) {

		StringBuffer sql = new StringBuffer();

		sql.append(
				" INSERT INTO trigger_detail(check_id,condition_detail,js_script,is_right,name,is_file,group_id,descript,tg_follow,tg_redeliv_interval,tg_max_redeliv,img_url) ");
		sql.append(
				" VALUES(:check_id,:condition_detail,:js_script,is_right,:name,:is_file,:group_id,:descript,:tg_follow,:tg_redeliv_interval,:tg_max_redeliv,:img_url)");

		SqlParameterSource param = new BeanPropertySqlParameterSource(t);

		KeyHolder holder = new GeneratedKeyHolder();

		getAppJdbcTemplate().update(sql.toString(), param, holder);

		return holder.getKey().longValue();
	}

	public int update(Trigger t) {

		StringBuffer sql = new StringBuffer();
		sql.append("update trigger_detail set update_time = NOW() ");
		if (t.getName() != null) {
			sql.append(" ,name = :name ");
		}
		if (t.getJs_script() != null) {
			sql.append(",js_script = :js_script ");
		}
		if (t.getIs_right() != null) {
			sql.append(",is_right = :is_right ");
		}
		if (!StringUtils.isEmpty(t.getCondition_detail())) {
			sql.append(",condition_detail = :condition_detail ");
		}
		if (t.getIs_file() != null) {
			sql.append(",is_file = :is_file ");
		}
		if (t.getDescript() != null) {
			sql.append(",descript = :descript ");
		}
		if (t.getTg_follow() != null) {
			sql.append(",tg_follow = :tg_follow ");
		}
		if (t.getTg_redeliv_interval() != null) {
			sql.append(",tg_redeliv_interval = :tg_redeliv_interval ");
		}
		if (t.getTg_max_redeliv() != null) {
			sql.append(",tg_max_redeliv = :tg_max_redeliv ");
		}
		if(t.getImg_url() != null){
			sql.append(",img_url = :img_url ");
		}
		sql.append(" WHERE id = :id ");

		SqlParameterSource param = new BeanPropertySqlParameterSource(t);

		return getAppJdbcTemplate().update(sql.toString(), param);
	}
	
	
	public List<Trigger> hotData(Set<String> arr,String userid) {

		StringBuffer sql = new StringBuffer();
		
		sql.append("	select td.*  ,IFNULL(su.`status`,0) is_sub                                              ");  
		sql.append("	 from trigger_detail td JOIN check_point cp                                             ");  
		sql.append("	ON td.check_id = cp.id AND cp.is_delete = 0   AND td.is_delete = 0  ");   

		Iterator<String> iterator = arr.iterator();
		
		StringBuffer orderby = new StringBuffer();
		
		if( arr.size() == 0){
			sql.append(" AND td.id =").append(iterator.next());
			orderby.append(iterator.next()).append(")");
		}else {
			sql.append(" AND td.id in (");
			
			int count = 0 ;
			while(iterator.hasNext()){
				count = count + 1;
				String temp = iterator.next();
				if(count == arr.size()){
					sql.append(temp).append(")");
					orderby.append(temp).append(")");
				}else {
					sql.append(temp).append(",");
					orderby.append(temp).append(",");
				}
				
			}
			
		}
		
		sql.append("  LEFT JOIN sub_user su ");
		sql.append(" 	ON td.id = su.trigger_id AND su.is_delete = 0 AND su.user_id = ");
		sql.append(userid);
		
		
		sql.append(" ORDER BY FIELD(td.id,");
		sql.append(orderby.toString());
		
		return getJdbcTemplate().query(sql.toString(), new Object[] {},
				new BeanPropertyRowMapper<Trigger>(Trigger.class));
	}
	
	
	
	public List<Trigger> queryDataFromGroup(String groupid,Integer is_follow,String search,String userid) {
		
		StringBuffer sql = new StringBuffer();
		
		sql.append(" 	select td.*  ,IFNULL(su.`status`,0) is_sub                                                                ");
		sql.append(" 	 from trigger_detail td JOIN check_point cp                                                               ");
		sql.append(" 	ON td.check_id = cp.id AND cp.is_delete = 0  AND td.is_delete = 0 ");
		if(is_follow != null){
			sql.append("	AND td.tg_follow = ").append(is_follow);
		}
		if(!StringUtils.isEmpty(search)){
			sql.append(" AND td.`name` LIKE '%").append(search).append("%'  ");
		}
		if(!StringUtils.isEmpty(groupid)){
			sql.append(" AND td.group_id = ").append(groupid);
		}
		
		sql.append("   LEFT JOIN sub_user su  ");
		sql.append(" ON td.id = su.trigger_id   and su.is_delete = 0    ");
		if(!StringUtils.isEmpty(userid)){
			sql.append(" AND su.user_id = ").append(userid);
		}	
			
			return getJdbcTemplate().query(sql.toString(), new Object[] {},
					new BeanPropertyRowMapper<Trigger>(Trigger.class));
	}

	
	
	public List<Trigger> querySelfData(String userid){
		
		StringBuffer sql = new StringBuffer();
		
		
		sql.append("  SELECT td.* FROM trigger_detail td ,sub_user su,check_point cp    "); 
		sql.append("  WHERE td.check_id = cp.id AND su.trigger_id = td.id               "); 
		sql.append("  AND cp.is_delete = 0 AND td.is_delete = 0 AND su.is_delete = 0    "); 
		sql.append("  AND su.`status` = 2                                                 "); 
		sql.append(" AND su.user_id = ");
		sql.append(userid);
		
		
		return getJdbcTemplate().query(sql.toString(), new Object[] {},
				new BeanPropertyRowMapper<Trigger>(Trigger.class));
	}
	
	
	
	
	
	
	
	
	
	
	
	
	

}
