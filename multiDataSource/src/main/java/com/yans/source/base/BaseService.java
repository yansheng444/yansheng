package com.yans.source.base;

import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.support.TransactionTemplate;

public class BaseService {
	
	public Logger logger = LoggerFactory.getLogger("business");
	
	public static final SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
	
	@Autowired
	private TransactionTemplate Template;

	public TransactionTemplate getTemplate() {
		return Template;
	}

	public void setTemplate(TransactionTemplate template) {
		Template = template;
	}
	
	
	

}
