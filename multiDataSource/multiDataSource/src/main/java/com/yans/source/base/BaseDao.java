package com.yans.source.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class BaseDao {

	public Logger logger = LoggerFactory.getLogger("business");
	
	@Autowired
	private NamedParameterJdbcTemplate appJdbcTemplate;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public NamedParameterJdbcTemplate getAppJdbcTemplate() {
		return appJdbcTemplate;
	}

	public void setAppJdbcTemplate(NamedParameterJdbcTemplate appJdbcTemplate) {
		this.appJdbcTemplate = appJdbcTemplate;
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

}
