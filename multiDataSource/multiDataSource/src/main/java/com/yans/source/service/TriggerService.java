package com.yans.source.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.yans.source.base.BaseService;
import com.yans.source.dao.PointTemplateDao;
import com.yans.source.dao.TriggerDao;
import com.yans.source.domain.PointTemplate;
import com.yans.source.domain.Trigger;

@Service
public class TriggerService extends BaseService{
	
	@Autowired
	private TriggerDao triggerDao;
	
	@Autowired
	private PointTemplateDao pointTemplateDao;
	
	@Autowired
	private TransactionTemplate template;
	
	@Transactional
	public int create(final Trigger t){
		
		
		return template.execute(new TransactionCallback<Integer>() {

			@Override
			public Integer doInTransaction(TransactionStatus arg0) {
				
				int res = 0 ;
				try{
					
					Long create = triggerDao.create(t);
					if(create < 0 ){
						arg0.setRollbackOnly();
						return res;
					}
					
					
					int create2 = pointTemplateDao.create(create, t.getTemplate());
					
					
					if(create2 > 0){
						res = 1;
					}else {
						arg0.setRollbackOnly();
						res = 0 ;
					}
					
					
				}catch(Exception e){
					res = -1;
					arg0.setRollbackOnly();
					logger.error("add trigger error:",e);
				}
				
				
				
				return res;
			}
		});
		
	}
	
	
	@Transactional
	public int update(final Trigger t){
		
		
		return template.execute(new TransactionCallback<Integer>() {

			@Override
			public Integer doInTransaction(TransactionStatus arg0) {
				
				int res = 0 ;
				try{
					
					int create = triggerDao.update(t);
					if(create < 0 ){
						arg0.setRollbackOnly();
						return res;
					}
					
					if(t.getTemplate() != null){
						
						PointTemplate p = new PointTemplate();
						p.setTrigger_id(t.getId());
						p.setMessage(t.getTemplate());
						
						int create2 = pointTemplateDao.update(p);
						
						if(create > 0 && create2 > 0 ){
							res = 1;
						}else{
							arg0.setRollbackOnly();
							return 0;
						}
					}else{
						if(create > 0 ){
							res = 1;
						}
					}
					
					
				}catch(Exception e){
					res = -1;
					logger.error("add trigger error:",e);
				}
				return res;
			}
		});
		
		
	}
	
	
}
