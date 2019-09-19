package com.yans.source.domain;

/**
 * 
 * @author yansheng
 *
 */
public class Trigger {
	
	private Long id ;
	private Long check_id ;
	private String condition_detail;
	private String js_script;
	private Integer is_right ; 
	private Integer is_delete ; 
	private String name ; 
	private Integer is_file;
	private Long group_id;
	private String descript ;
	private Integer tg_follow;
	private Integer tg_redeliv_interval;
	private Integer tg_max_redeliv;
	private String b_user_id;
	private transient String template;
	private transient Integer is_sub;
	private String img_url;
	
	
	
	
	
	public String getImg_url() {
		return img_url;
	}
	public void setImg_url(String img_url) {
		this.img_url = img_url;
	}
	public Integer getIs_sub() {
		return is_sub;
	}
	public void setIs_sub(Integer is_sub) {
		this.is_sub = is_sub;
	}
	public String getB_user_id() {
		return b_user_id;
	}
	public void setB_user_id(String b_user_id) {
		this.b_user_id = b_user_id;
	}
	public String getTemplate() {
		return template;
	}
	public void setTemplate(String template) {
		this.template = template;
	}
	public String getCondition_detail() {
		return condition_detail;
	}
	public void setCondition_detail(String condition_detail) {
		this.condition_detail = condition_detail;
	}
	public String getJs_script() {
		return js_script;
	}
	public void setJs_script(String js_script) {
		this.js_script = js_script;
	}
	public Long getGroup_id() {
		return group_id;
	}
	public void setGroup_id(Long group_id) {
		this.group_id = group_id;
	}
	public String getDescript() {
		return descript;
	}
	public void setDescript(String descript) {
		this.descript = descript;
	}
	public Integer getTg_follow() {
		return tg_follow;
	}
	public void setTg_follow(Integer tg_follow) {
		this.tg_follow = tg_follow;
	}
	public Integer getTg_redeliv_interval() {
		return tg_redeliv_interval;
	}
	public void setTg_redeliv_interval(Integer tg_redeliv_interval) {
		this.tg_redeliv_interval = tg_redeliv_interval;
	}
	public Integer getTg_max_redeliv() {
		return tg_max_redeliv;
	}
	public void setTg_max_redeliv(Integer tg_max_redeliv) {
		this.tg_max_redeliv = tg_max_redeliv;
	}
	public Integer getIs_file() {
		return is_file;
	}
	public void setIs_file(Integer is_file) {
		this.is_file = is_file;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getIs_delete() {
		return is_delete;
	}
	public void setIs_delete(Integer is_delete) {
		this.is_delete = is_delete;
	}
	public Integer getIs_right() {
		return is_right;
	}
	public void setIs_right(Integer is_right) {
		this.is_right = is_right;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getCheck_id() {
		return check_id;
	}
	public void setCheck_id(Long check_id) {
		this.check_id = check_id;
	}
	
	
	
	
}
