package com.dummy.myerp.consumer;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class App {
	
	public static void main( String[] args )
    {
//		boolean b = Pattern.matches("\\d{1,5}-\\d{4}/\\d{5}", "AC-2016/00001");
//		boolean b = Pattern.matches("[A-Z]{2}-\\d{4}/\\d{5}", "AC-2019/00245");
//		System.out.println(b);
		Calendar calendar = Calendar.getInstance();
	  	System.out.println("calendar.getInstance:"+calendar.getTime().toString());
	  	 
	  	calendar.set(2017, 12, 31);
	  	System.out.println("calendar:"+calendar.getTime().toString());
  
	  	Calendar calendar2 = new GregorianCalendar(2014, 9, 31);
	  	System.out.println(calendar2.getTime().toString());
    }


}
