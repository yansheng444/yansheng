package com.zg.base;

import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.support.TransactionTemplate;

public class BaseService implements LogProvider,GsonProvider {
	
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
