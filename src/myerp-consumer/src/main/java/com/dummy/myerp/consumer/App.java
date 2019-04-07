package com.dummy.myerp.consumer;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.dummy.myerp.consumer.dao.contrat.ComptabiliteDao;
import com.dummy.myerp.consumer.dao.contrat.DaoProxy;
import com.dummy.myerp.model.bean.comptabilite.CompteComptable;

public class App {
	
	public static void main( String[] args )
    {
    	System.out.println( "Hello DAO !" );

		ApplicationContext ctx = new ClassPathXmlApplicationContext(new String[] { "/**/applicationContext.xml"});  
		DaoProxy daoProxy = (DaoProxy) ctx.getBean("DaoProxy");
		ComptabiliteDao dao = daoProxy.getComptabiliteDao();
		List<CompteComptable> comptes = dao.getListCompteComptable();
    }


}
