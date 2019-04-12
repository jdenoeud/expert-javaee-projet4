package com.dummy.myerp.consumer;

import java.util.Calendar;
import java.util.Date;

public class App {
	
	public static void main( String[] args )
    {
    	System.out.println( "Hello DAO !" );
//
//		ApplicationContext ctx = new ClassPathXmlApplicationContext(new String[] { "/**/testApplicationContext.xml"});  
//		DaoProxy daoProxy = (DaoProxy) ctx.getBean("DaoProxy");
//		ComptabiliteDao dao = daoProxy.getComptabiliteDao();
//		List<JournalComptable> journaux = dao.getListJournalComptable();
//		for(JournalComptable j : journaux) {
//			System.out.println(j.getCode());
//			System.out.println(j.getLibelle());
//		}
    	
    	Calendar c1 = Calendar.getInstance(); 	  
        // set Month 
        // MONTH starts with 0 i.e. ( 0 - Jan) 
        c1.set(Calendar.MONTH, 04);   
        // set Date 
        c1.set(Calendar.DATE, 12);   
        // set Year 
        c1.set(Calendar.YEAR, 2019);   
        // creating a date object with specified time. 
        Date dateTest = c1.getTime(); 
        System.out.println(dateTest);
  
    }


}
