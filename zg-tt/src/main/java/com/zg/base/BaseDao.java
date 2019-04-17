package com.zg.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;


public class BaseDao implements LogProvider {

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
